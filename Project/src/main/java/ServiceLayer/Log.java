package main.java.ServiceLayer;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Log {

        static private class LogHolder {
            static Log log = null;

            static {
                try {
                    log = new Log();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static Log getInstance() {
            return LogHolder.log;
        }

        private Logger event_logger;
        private Logger error_logger;
        private FileHandler fh1;
        private FileHandler fh2;
        private File f1;
        private File f2;

        private Log() throws IOException {
            f1 = new File("event_log.txt");
            fh1 = new FileHandler("event_log.txt"); // for append, add true as 2nd arg
            event_logger = Logger.getLogger("event_log.txt");
            event_logger.addHandler(fh1);
            event_logger.setLevel(Level.INFO);
            SimpleFormatter formatter = new SimpleFormatter();
            fh1.setFormatter(formatter);

            File f2 = new File("error_log.txt");
            fh2 = new FileHandler("error_log.txt"); // for append, add true as 2nd arg
            error_logger = Logger.getLogger("error_log.txt");
            error_logger.addHandler(fh2);
            error_logger.setLevel(Level.SEVERE);
            fh2.setFormatter(formatter);
        }

        public void event(String msg) {
            event_logger.info(msg);
        }

        public void error(String msg) {
            error_logger.severe(msg);
        }
}
