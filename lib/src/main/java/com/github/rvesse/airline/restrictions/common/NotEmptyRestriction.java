package com.github.rvesse.airline.restrictions.common;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.utils.AirlineUtils;

public class NotEmptyRestriction extends AbstractStringRestriction {

    @Override
    protected boolean isValid(String value) {
        return !StringUtils.isEmpty(value);
    }

    @Override
    protected <T> ParseRestrictionViolatedException violated(ParseState<T> state, OptionMetadata option, String value) {
        return new ParseRestrictionViolatedException("Option '%s' requires a non-empty value", option.getTitle());
    }

    @Override
    protected <T> ParseRestrictionViolatedException violated(ParseState<T> state, ArgumentsMetadata arguments,
            String value) {
        return new ParseRestrictionViolatedException("Arguments '%s' requires a non-empty value",
                AirlineUtils.first(arguments.getTitle()));
    }

}
