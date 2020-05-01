package mat.cql;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * A factory for the cql visitor prototype creation.
 * This is currently a spring service so it can return prototypes in future versions.
 * ConversionCqlToMatXml and CqlToMatXml will need to be prototype beans capable of having DAOs.
 */
@Service
@Configuration
public class CqlVisitorFactory {
    /**
     * Creates a new CqlToMatXml prototype.
     * @return A new CqlToMatXml bean.
     */
    @Lookup
    public CqlToMatXml getCqlToMatXmlVisitor() {
        return null;
    }
}
