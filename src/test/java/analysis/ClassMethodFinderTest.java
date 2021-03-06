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
package analysis;

import analysis.MethodAnalyzer.ClassMethodFinder;
import analysis.MethodAnalyzer.MethodDescriber;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.fail;

public class ClassMethodFinderTest {

    private ResourceExampleClassParser _loader;
    CompilationUnit _cu;

    @Before
    public void Setup()
    {
         _loader = new ResourceExampleClassParser();
    }

    private void CreateCompilationUnitFromTestClass(String classTemplate)
    {
        _cu = _loader.Parse(classTemplate);
    }

    @Test
    public void GivenAClassRetrieveAllDefinedMethods()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        List<String> allMethods = cmf.getAllDefinedMethods();
        Assert.assertEquals(2, allMethods.size());
    }

    @Test
    public void GivenALocationInAClassDetermineIfLocatedInMethod()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");

        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu,"TwoMethodClass");

                // Based on the comment lines in SimpleClassWith2Methods, saying in or outside class check if it meets
        // Lines outside method
        Assert.assertFalse(cmf.isLocationInMethod(3));
        Assert.assertFalse(cmf.isLocationInMethod(9));
        // Lines inside method
        Assert.assertTrue(cmf.isLocationInMethod(4));
        Assert.assertTrue(cmf.isLocationInMethod(6));
    }

    @Test
    public void GivenALocationInsideAMethodReturnNameOfMethod()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals("MethodOne", cmf.getMethodDescriberForLocation(6).getName());
        Assert.assertEquals("MethodTwo", cmf.getMethodDescriberForLocation(12).getName());
    }

    @Test
    public void GivenALocationOutsideMethodScopeReturnsEmptyString()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        Assert.assertEquals("", cmf.getMethodDescriberForLocation(14).getName());
    }

    @Test
    public void GivenClassSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("SimpleClassWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "TwoMethodClass");

        MethodDescriber method = new MethodDescriber("void","MethodTwo","()");

        Assert.assertEquals(true, cmf.hasMethodDefined(method));
    }

    @Test
    public void GivenInterfaceSpecificMethodCanBeFound()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "D");

        MethodDescriber method = new MethodDescriber("void","MethodFour","()");
        Assert.assertEquals(true, cmf.hasMethodDefined(method));
    }

    @Test
    public void GivenMethodIfDefinedInInterfaceIsDetectedWhenAskedFor()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try {
            MethodDescriber method = new MethodDescriber("void","MethodOne","()");
            Assert.assertTrue(cmf.isMethodDeclaredFirstTimeInInterface(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodIsDetectedIfDefinedEarlierInSuperclass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            MethodDescriber method = new MethodDescriber("void","MethodOne","()");
            Assert.assertTrue(cmf.isMethodDefinedInSuperClass(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInInterface()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try {
            MethodDescriber method = new MethodDescriber("void","MethodTwo","()");
            Assert.assertFalse(cmf.isMethodDeclaredFirstTimeInInterface(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }

    }

    @Test
    public void GivenMethodDeclaredLocalIsNotDetectedWhenTryingToFindInSuperClass()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            MethodDescriber method = new MethodDescriber("void","MethodTwo","()");
            Assert.assertFalse(cmf.isMethodDefinedInSuperClass(method));
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void GivenMethodDlistsalldeclarations()
    {
        CreateCompilationUnitFromTestClass("ExtendedClassA_BWith2Methods.java.txt");
        ClassMethodFinder cmf = new ClassMethodFinder();
        cmf.initialize(_cu, "A");

        try
        {
            List<String> lst = cmf.getAllDefinedMethods();
            Assert.assertEquals(4, lst.size());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }
}
