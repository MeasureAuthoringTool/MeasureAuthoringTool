package mat.server.service.jobs;

import lombok.extern.slf4j.Slf4j;
import mat.model.CQLAuditLog;
import mat.model.clause.CQLLibrary;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CQLLibraryTimestampUpdateJob {

    private static final String CQL_LIBRARY = "cqlLibrary";

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void execute() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        final Runnable task = new Runnable() {

            @Override
            public void run() {
                log.debug("Job to Update Library Timestamp STARTED: " + LocalDateTime.now());
                final Instant start = Instant.now();
                updateLibrariesWithNoTimestamp();
                final Instant finish = Instant.now();
                final long timeElapsed = Duration.between(start, finish).toMillis();
                log.debug("TIME ELAPSED: " + timeElapsed);
                log.debug("Job to Update Library Timestamp ENDED: " + LocalDateTime.now());

                if (!scheduler.isShutdown()) {
                    scheduler.shutdown();
                }

            }
        };

        scheduler.schedule(task, 30, TimeUnit.SECONDS);

    }

    private void updateLibrariesWithNoTimestamp() {

        try (final Session session = sessionFactory.openSession()) {

            final CriteriaBuilder cb = session.getCriteriaBuilder();
            final CriteriaQuery<CQLAuditLog> query = cb.createQuery(CQLAuditLog.class);
            final Root<CQLAuditLog> root = query.from(CQLAuditLog.class);

            final Join<CQLAuditLog, CQLLibrary> join = root.join(CQL_LIBRARY);

            query.select(cb.construct(
                    CQLAuditLog.class,
                    root.get(CQL_LIBRARY),
                    cb.greatest(root.<Date>get("time"))));

            query.where(cb.isNull(join.get("lastModifiedOn")));

            query.groupBy(root.get(CQL_LIBRARY).get("id"));

            final List<CQLAuditLog> librariesLogList = session.createQuery(query).getResultList();

            if (librariesLogList != null) {
                updateLibraryModificationData(librariesLogList);
                log.debug("Updated timestamp for " + librariesLogList.size() + " libraries");
            }

        } catch (final Exception e) {
            log.error("updateLibrariesWithNoTimestamp",e);
        }
    }

    @Transactional
    public void updateLibraryModificationData(List<CQLAuditLog> librariesLogList) {
        try (final Session session = sessionFactory.openSession()) {

            final Transaction transaction = session.beginTransaction();

            log.debug("Update Library Timestamp TRANSACTION START");

            int count = 0;
            final int batchSize = 50;

            for (final CQLAuditLog libraryLog : librariesLogList) {
                final CriteriaBuilder cb = session.getCriteriaBuilder();
                final CriteriaUpdate<CQLLibrary> update = cb.createCriteriaUpdate(CQLLibrary.class);
                final Root<CQLLibrary> root = update.from(CQLLibrary.class);
                update.set("lastModifiedOn", new java.sql.Timestamp(libraryLog.getTime().getTime()).toLocalDateTime());
                update.where(cb.equal(root.get("id"), libraryLog.getCqlLibrary().getId()));
                session.createQuery(update).executeUpdate();
                if (++count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();

            log.debug("Update Library Timestamp TRANSACTION END");

        } catch (final Exception e) {
            log.error("updateLibraryModificationData",e);
        }

    }

}
