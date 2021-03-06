package com.codahale.dropwizard.migrations;

import com.codahale.dropwizard.Configuration;
import com.codahale.dropwizard.db.ConfigurationStrategy;
import liquibase.Liquibase;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class DbLocksCommand<T extends Configuration> extends AbstractLiquibaseCommand<T> {
    public DbLocksCommand(ConfigurationStrategy<T> strategy, Class<T> configurationClass) {
        super("locks", "Manage database migration locks", strategy, configurationClass);
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);

        subparser.addArgument("-l", "--list")
                 .dest("list")
                 .action(Arguments.storeTrue())
                 .setDefault(Boolean.FALSE)
                 .help("list all open locks");

        subparser.addArgument("-r", "--force-release")
                 .dest("release")
                 .action(Arguments.storeTrue())
                 .setDefault(Boolean.FALSE)
                 .help("forcibly release all open locks");
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void run(Namespace namespace, Liquibase liquibase) throws Exception {
        final Boolean list = namespace.getBoolean("list");
        final Boolean release = namespace.getBoolean("release");

        if (!list && !release) {
            throw new IllegalArgumentException("Must specify either --list or --force-release");
        } else if (list) {
            liquibase.reportLocks(System.out);
        } else {
            liquibase.forceReleaseLocks();
        }
    }
}
