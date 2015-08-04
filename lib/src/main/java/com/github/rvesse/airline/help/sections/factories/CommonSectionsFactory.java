/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.help.Discussion;
import com.github.rvesse.airline.annotations.help.Examples;
import com.github.rvesse.airline.annotations.help.ExitCodes;
import com.github.rvesse.airline.help.sections.HelpSection;
import com.github.rvesse.airline.help.sections.common.DiscussionSection;
import com.github.rvesse.airline.help.sections.common.ExamplesSection;
import com.github.rvesse.airline.help.sections.common.ExitCodesSection;

/**
 * A help section factory that implements the common sections built into Airline
 */
public class CommonSectionsFactory implements HelpSectionFactory {

    @Override
    public HelpSection createSection(Annotation annotation) {
        if (annotation instanceof Examples) {
            Examples ex = (Examples) annotation;
            return new ExamplesSection(ex.examples(), ex.descriptions());
        } else if (annotation instanceof Discussion) {
            return new DiscussionSection(((Discussion) annotation).paragraphs());
        } else if (annotation instanceof ExitCodes) {
            ExitCodes exits = (ExitCodes) annotation;
            return new ExitCodesSection(exits.codes(), exits.descriptions());
        }
        return null;
    }

}
