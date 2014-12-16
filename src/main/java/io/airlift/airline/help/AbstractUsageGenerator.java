package io.airlift.airline.help;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static io.airlift.airline.model.OptionMetadata.isHiddenPredicate;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class AbstractUsageGenerator {

    private final Comparator<? super OptionMetadata> optionComparator;
    private final Comparator<? super CommandMetadata> commandComparator;

    public AbstractUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR);
    }

    public AbstractUsageGenerator(Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator) {
        this.optionComparator = optionComparator;
        this.commandComparator = commandComparator;
    }
    
    protected final Comparator<? super OptionMetadata> getOptionComparator() {
        return this.optionComparator;
    }
    
    protected final Comparator<? super CommandMetadata> getCommandComparator() {
        return this.commandComparator;
    }

    /**
     * Sorts the options assuming a non-null comparator was provided at
     * instantiation time
     * 
     * @param options
     *            Options
     * @return Sorted options
     */
    protected List<OptionMetadata> sortOptions(List<OptionMetadata> options) {
        if (optionComparator != null) {
            options = new ArrayList<OptionMetadata>(options);
            Collections.sort(options, optionComparator);
        }
        return options;
    }

    /**
     * Sorts the commands assuming a non-null comparator was provided at
     * instantiation time
     * 
     * @param commands
     *            Commands
     * @return Sorted commands
     */
    protected List<CommandMetadata> sortCommands(List<CommandMetadata> commands) {
        if (commandComparator != null) {
            commands = new ArrayList<>(commands);
            Collections.sort(commands, commandComparator);
        }
        return commands;
    }

    /**
     * HTMLizes a string i.e. escapes HTML special characters into HTML entities
     * and new lines into HTML line breaks
     * 
     * @param value
     *            String to HTMLize
     * @return HTMLized string
     */
    protected final String htmlize(final String value) {
        return value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>");
    }

    /**
     * Converts a command into the default command representation for the usage
     * documentation
     * 
     * @param command
     *            Default command name
     * @return
     */
    protected String toDefaultCommand(String command) {
        if (Strings.isNullOrEmpty(command)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        stringBuilder.append(command);
        stringBuilder.append(" ]");

        return stringBuilder.toString();
    }

    /**
     * Converts the options into their synopsis representation for the usage
     * documentation
     * 
     * @param options
     *            Options
     * @return
     */
    protected List<String> toSynopsisUsage(List<OptionMetadata> options) {
        return ImmutableList.copyOf(transform(filter(options, isHiddenPredicate()),
                new Function<OptionMetadata, String>() {
                    public String apply(OptionMetadata option) {
                        if (option.isHidden()) {
                            return "";
                        }

                        return toUsage(option);
                    }
                }));
    }

    protected String toUsage(ArgumentsMetadata arguments) {
        if (!arguments.getUsage().isEmpty()) {
            return arguments.getUsage();
        }

        boolean required = arguments.isRequired();
        StringBuilder stringBuilder = new StringBuilder();
        if (!required) {
            // TODO: be able to handle required arguments individually, like
            // arity for the options
            stringBuilder.append("[ ");
        }

        stringBuilder.append(toDescription(arguments));

        if (arguments.isMultiValued()) {
            stringBuilder.append("...");
        }

        if (!required) {
            stringBuilder.append(" ]");
        }
        return stringBuilder.toString();
    }

    protected String toUsage(OptionMetadata option) {
        Set<String> options = option.getOptions();
        boolean required = option.isRequired();
        StringBuilder stringBuilder = new StringBuilder();
        if (!required) {
            stringBuilder.append("[ ");
        }

        if (options.size() > 1) {
            stringBuilder.append('{');
        }

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = Joiner.on(" ").join(
                    transform(ImmutableList.of(option.getTitle()), new Function<String, String>() {
                        public String apply(@Nullable String argument) {
                            return "<" + argument + ">";
                        }
                    }));
        } else {
            argumentString = null;
        }

        Joiner.on(" | ").appendTo(stringBuilder, transform(options, new Function<String, String>() {
            public String apply(@Nullable String option) {
                // if (argumentString != null) {
                // return option + " " + argumentString;
                // }
                // else {
                return option;
                // }
            }
        }));

        if (options.size() > 1) {
            stringBuilder.append('}');
        }

        if (argumentString != null) {
            stringBuilder.append(" " + argumentString);
        }

        if (option.isMultiValued()) {
            stringBuilder.append("...");
        }

        if (!required) {
            stringBuilder.append(" ]");
        }
        return stringBuilder.toString();
    }

    protected String toDescription(ArgumentsMetadata arguments) {
        if (!arguments.getUsage().isEmpty()) {
            return arguments.getUsage();
        }
        List<String> descriptionTitles = arguments.getTitle();
        StringBuilder stringBuilder = new StringBuilder();
        for (String title : descriptionTitles) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("<");
            stringBuilder.append(title);
            stringBuilder.append(">");
        }

        return stringBuilder.toString();

    }

    protected String toDescription(OptionMetadata option) {
        Set<String> options = option.getOptions();
        StringBuilder stringBuilder = new StringBuilder();

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = Joiner.on(" ").join(
                    Lists.transform(ImmutableList.of(option.getTitle()), new Function<String, String>() {
                        public String apply(@Nullable String argument) {
                            return "<" + argument + ">";
                        }
                    }));
        } else {
            argumentString = null;
        }

        Joiner.on(", ").appendTo(stringBuilder, transform(options, new Function<String, String>() {
            public String apply(@Nullable String option) {
                if (argumentString != null) {
                    return option + " " + argumentString;
                }
                return option;
            }
        }));

        return stringBuilder.toString();
    }

}