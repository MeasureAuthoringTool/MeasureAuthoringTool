package mat.server.service.impl;

        import org.springframework.beans.factory.annotation.Lookup;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.stereotype.Service;

@Service
@Configuration
public class ZipPackagerFactory {

    @Lookup
    public ZipPackager getZipPackager() {
        return null;
    }
}
