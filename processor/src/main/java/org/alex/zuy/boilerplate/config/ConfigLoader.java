
package org.alex.zuy.boilerplate.config;

import java.net.URL;
import java.util.Optional;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.alex.zuy.boilerplate.config.generated.Configuration;
import org.xml.sax.SAXException;

public class ConfigLoader {

    private static final String PATH_CONFIGURATION_SCHEMA = "org/alex/zuy/boilerplate/config-schema.xsd";

    public Configuration loadConfig(URL configUrl) {
        CollectingValidationEventHandler validationEventHandler = new CollectingValidationEventHandler();
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(loadConfigurationSchema());
            unmarshaller.setEventHandler(validationEventHandler);
            return (Configuration) unmarshaller.unmarshal(configUrl);
        }
        catch (UnmarshalException e) {
            throw validationEventHandler.buildConfigValidationException();
        }
        catch (SAXException | JAXBException e) {
            throw new ConfigException(e);
        }
    }

    private Schema loadConfigurationSchema() throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL schemaUrl = getClass().getClassLoader().getResource(PATH_CONFIGURATION_SCHEMA);
        return schemaFactory.newSchema(schemaUrl);
    }

    private static final class CollectingValidationEventHandler implements ValidationEventHandler {

        private ValidationEvent event;

        @Override
        public boolean handleEvent(ValidationEvent validationEvent) {
            event = validationEvent;
            return false;
        }

        public ConfigValidationException buildConfigValidationException() {
            ValidationEventLocator locator = event.getLocator();
            ImmutableConfigurationLocation location = ImmutableConfigurationLocation.builder()
                .columnNumber(locator.getColumnNumber())
                .lineNumber(locator.getLineNumber())
                .configurationElement(Optional.ofNullable(locator.getNode())
                    .map(node -> String.format("XML node \"%s\"", node.getNodeName())))
                .build();
            return new ConfigValidationException(event.getMessage(), event.getLinkedException(), location);
        }
    }
}
