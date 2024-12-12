package com.hyeonmusic.MySongSpace.config;


import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

import static org.hibernate.type.StandardBasicTypes.DOUBLE;

public class CustomFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME = "match_against";
    private static final String FUNCTION_PATTERN = "match(?1, ?2) against (?3 in boolean mode)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .registerPattern(FUNCTION_NAME, FUNCTION_PATTERN,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE));
    }
}
