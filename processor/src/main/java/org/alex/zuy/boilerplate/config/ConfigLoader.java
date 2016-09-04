
package org.alex.zuy.boilerplate.config;

import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.alex.zuy.boilerplate.config.generated.Configuration;

public class ConfigLoader {

    public Configuration loadConfig(URL configUrl) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Configuration) unmarshaller.unmarshal(configUrl);
        }
        catch (JAXBException e) {
            throw new ConfigException(e);
        }
    }
}
