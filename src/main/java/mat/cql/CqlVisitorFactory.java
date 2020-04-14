package mat.cql;

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
     * Creates a new ConversionCqlToMatXml bean each time this method is called.
     *
     * @return A new ConversionCqlToMatXml bean.
     */
    public ConversionCqlToMatXml getConversionCqlToMatXmlVisitor() {
        return new ConversionCqlToMatXml();
    }

    /**
     * Creates a new CqlToMatXml bean each time this method is called.
     *
     * @return Creates a new CqlToMatXml as a spring prototype bean.
     */
    public CqlToMatXml getCqlToMatXmlVisitor() {
        return new CqlToMatXml();
    }
}
