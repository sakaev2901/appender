package itis;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

// аннотация просто задает нужные пармметры
@Plugin(
        name = "JdbcAppender", // имя янашего аппендера. нужен чтобы наш log4j2.xml нашел этот класс
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE // говорим опять же нашей xml что этот класс есть аппендер
)
public class JdbcAppender  extends AbstractAppender {

    private final static ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig(); // для jdbc


    protected JdbcAppender(String name) {
        // не все атрбуты нужны нам. поэтом null
        // атрибут name передается в конструктор AbstractAppender, который как раз таки нужен для нахождения этого аппендера
        super(name, null, null, false, null);
    }

    @PluginFactory // нужно еще создать статичный метод(фабричный метод) для того чтобы
    // система смогла достать объект этого класса. PluginFactory говорит что этот
    // метод возвращает этот объект
    public static JdbcAppender createAppender(@PluginAttribute("name") String name) {
        return new JdbcAppender(name);
    }

    @Override // переопределяем логику хранения каждого лога
    public void append(LogEvent logEvent) {
        saveLog(logEvent);
    }

    private void saveLog(LogEvent logEvent) {
        Connection connection = CONNECTION_CONFIG.getConnection(); // здесь наверное все понятно. просто логка jdbc
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into logs values(?, ?, ?)");
            preparedStatement.setString(1, logEvent.getMessage().getFormattedMessage());
            preparedStatement.setString(2, logEvent.getLevel().name());
            preparedStatement.setString(3, new Date().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
