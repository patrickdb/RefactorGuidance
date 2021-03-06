/**
 *  This file is part of RefactorGuidance project. Which explores possibilities to generate context based
 *  instructions on how to refactor a piece of Java code. This applied in an education setting (bachelor SE students)
 *
 *      Copyright (C) 2018, Patrick de Beer, p.debeer@fontys.nl
 *
 *          This program is free software: you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation, either version 3 of the License, or
 *          (at your option) any later version.
 *
 *          This program is distributed in the hope that it will be useful,
 *          but WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *          GNU General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package aig;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class InstructionGeneratorTests {

    @Test
    public void GivenNoContextResultsInEmptyInstructions()
    {
        AdaptiveInstructionGraph graph = new AIG_TestGenerator().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: Context-set is empty or null");
    }

    @Test
    public void GivenGraphNullGivesErrorInstruction()
    {
        InstructionGenerator generator = new InstructionGenerator(null);
        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: AIT is null");
    }

    @Test
    public void GivenEmptyContextSetShouldResultInErrorInstruction()
    {
        AdaptiveInstructionGraph graph = new AIG_TestGenerator().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);
        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.noneOf(CodeContext.CodeContextEnum.class);
        generator.setContext(codeContext);

        Assert.assertEquals(generator.generateInstruction().get(0), "ERROR: Context-set is empty or null");
    }

    @Test
    public void GivenInstructionAndParameterMapFillsParameterValues()
    {
        AdaptiveInstructionGraph graph = new AIG_TestGenerator().getAdaptiveInstructionGraph();
        InstructionGenerator generator = new InstructionGenerator(graph);

        // Instruction in template Parameter fill test: Dummy method $method is located in dummy $class
        Map<String, List<String>> parameterMap = new HashMap<>();
        parameterMap.put("#method", Arrays.asList("printHelloWorld"));
        parameterMap.put("#class", Arrays.asList("Hello"));

        EnumSet<CodeContext.CodeContextEnum> codeContext = EnumSet.of(CodeContext.CodeContextEnum.MethodOverride);

        generator.setParameterMap(parameterMap);
        generator.setContext(codeContext);

        List<String> instructionSteps = generator.generateInstruction();
        Assert.assertEquals("Parameter fill test: Dummy method printHelloWorld is located in dummy Hello ", instructionSteps.get(0));
        Assert.assertEquals("Instruction 3: Overrides class Hello ", instructionSteps.get(1));
    }

    @Test
    public void defaultNoRiskContextDescribed()
    {
        AdaptiveInstructionGraph graph = new AIG_TestGenerator().getAdaptiveInstructionGraph();
        Assert.assertEquals(0, graph.getSetOfRiskContext().size());
        Assert.assertEquals("", graph.allInstructions.get(0).decisions.get(0).getRiskDescription());
    }

    @Test
    public void graphWithRiskDescriptions()
    {
        AdaptiveInstructionGraph graph = new AIG_TestGenerator().getAdaptiveInstructionGraphWithRiskDescription();
        Assert.assertEquals(1, graph.getSetOfRiskContext().size());
        Assert.assertFalse(graph.allInstructions.get(0).decisions.get(1).getRiskDescription().isEmpty());
        Assert.assertTrue(graph.allInstructions.get(0).decisions.get(0).getRiskDescription().isEmpty());
    }
}
