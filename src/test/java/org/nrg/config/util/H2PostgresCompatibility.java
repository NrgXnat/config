/*
 * config: org.nrg.config.util.H2PostgresCompatibility
 * XNAT http://www.xnat.org
 * Copyright (c) 2026, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 */

package org.nrg.config.util;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * H2 stand-ins for the Postgres functions used by DefaultConfigService.lockForReplace(). The unit-test
 * database is H2 (see OrmTestConfiguration), which doesn't provide pg_advisory_xact_lock() or hashtext().
 * The tests run single-threaded against a per-JVM in-memory database, so the lock can be a no-op here:
 * these aliases only need to keep the SQL valid. Register via {@link #register(JdbcTemplate)} from every
 * test context that exercises the config service.
 */
public final class H2PostgresCompatibility {
    private H2PostgresCompatibility() {
    }

    public static void register(final JdbcTemplate template) {
        template.execute("CREATE ALIAS IF NOT EXISTS hashtext FOR \"org.nrg.config.util.H2PostgresCompatibility.hashtext\"");
        template.execute("CREATE ALIAS IF NOT EXISTS pg_advisory_xact_lock FOR \"org.nrg.config.util.H2PostgresCompatibility.pgAdvisoryXactLock\"");
    }

    public static int hashtext(final String value) {
        return value == null ? 0 : value.hashCode();
    }

    @SuppressWarnings("unused")
    public static Integer pgAdvisoryXactLock(final int key) {
        return null;
    }
}
