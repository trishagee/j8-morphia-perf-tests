package com.mechanitis.undertest;

import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;

import static java.lang.String.format;

public class LogLine implements Comparable<LogLine> {
    private final ConstraintViolation v;

    LogLine(final ConstraintViolation v) {
        this.v = v;
    }

    @Override
    public int compareTo(final LogLine o) {
        return v.getPrefix().compareTo(o.v.getPrefix());
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LogLine logLine = (LogLine) o;

        return v.equals(logLine.v);

    }

    void log(final Logger logger) {
        switch (v.getLevel()) {
            case SEVERE:
                logger.error(v.render());
                break;
            case WARNING:
                logger.warning(v.render());
                break;
            case INFO:
                logger.info(v.render());
                break;
            case MINOR:
                logger.debug(v.render());
                break;
            default:
                throw new IllegalStateException(format("Cannot log %s of Level %s", ConstraintViolation.class.getSimpleName(),
                        v.getLevel()));
        }
    }
}
