package context;

import aig.CodeContext;
import analysis.JavaParserTestSetup;
import analysis.context.*;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class intraMethodContextTests extends JavaParserTestSetup {

    double _hiddenName = 0.0;

    @Test
    public void detectNoneLocalVarDependencies()
    {
        try {
            extractRegion(7, 10);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractNoneLocalDependencies nlvdCtxt = new MethodExtractNoneLocalDependencies(cc);

            assertEquals(true, nlvdCtxt.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoneLocalDependencies, nlvdCtxt.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectSingleArgument()
    {
            try {
                extractRegion(19,22);
                setupTestClass("ExtractMethodCases", "ExtractionWith1Input");

                ContextConfiguration cc = mdfaAnalysis();
                MethodExtractSingleArgument mesp = new MethodExtractSingleArgument(cc);

                Assert.assertEquals(true, mesp.detect());
                retrieveParams(mesp);

                Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("a"));
                Assert.assertEquals(1, _params.get(_pc.getArgumentListType()).size());

                Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractSingleArgument, mesp.getType());
            }
            catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
    }

    @Test
    public void detectSingleResult()
    {
        try {
            extractRegion(43,46);
            setupTestClass("ExtractMethodCases", "ExtractionWith1Output");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractSingleResult mesr = new MethodExtractSingleResult(cc);

            Assert.assertEquals(true, mesr.detect());
            retrieveParams(mesr);

            Assert.assertTrue(_params.get(_pc.getResultListType()).contains("b"));
            Assert.assertEquals(1, _params.get(_pc.getResultListType()).size());

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractSingleResult, mesr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectMultiArgument()
    {
        try {
            extractRegion(93,96);
            setupTestClass("ExtractMethodCases", "ExtractionWith2Input1NoneRelated");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractMultiArgument mema = new MethodExtractMultiArgument(cc);

            Assert.assertEquals(true, mema.detect());
            retrieveParams(mema);

            Assert.assertNotNull(_params.get(_pc.getArgumentListType()));

            Assert.assertEquals(2, _params.get(_pc.getArgumentListType()).size());
            Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("a"));
            Assert.assertTrue(_params.get(_pc.getArgumentListType()).contains("b"));

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractMultiArgument, mema.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectMultiResult()
    {
        try {
            extractRegion(105,112);
            setupTestClass("ExtractMethodCases", "ExtractWith2OutputAnd2NoneRelated");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractMultiResult memr = new MethodExtractMultiResult(cc);

            Assert.assertEquals(true, memr.detect());
            retrieveParams(memr);

            Assert.assertNotNull(_params.get(_pc.getResultListType()));

            Assert.assertEquals(2, _params.get(_pc.getResultListType()).size());
            Assert.assertTrue(_params.get(_pc.getResultListType()).contains("a"));
            Assert.assertTrue(_params.get(_pc.getResultListType()).contains("b"));

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractMultiResult, memr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectNoneArgumentsNeeded()
    {
        try {
            extractRegion(7, 10);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractNoneArguments mena = new MethodExtractNoneArguments(cc);

            assertEquals(true, mena.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoneArguments, mena.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectNoneResultsNeeded()
    {
        try {
            extractRegion(7, 10);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = mdfaAnalysis();
            MethodExtractNoneResults menr = new MethodExtractNoneResults(cc);

            assertEquals(true, menr.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoneResults, menr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectLocalNameHiding()
    {
        try {
            extractRegion(126, 129);
            setupTestClass("ExtractMethodCases", "ExtractionWithLocalVarHidingClassField");

            ContextConfiguration cc = mdfaAnalysis();
            cc.setClassName("ExtractMethodCases");
            cc.setCompilationUnit(_cu);

            MethodExtractNameHiding menh = new MethodExtractNameHiding(cc);

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNameHiding, menh.getType());
            assertEquals(true, menh.detect());
            retrieveParams(menh);

            Assert.assertEquals(1, _params.get(_pc.getVariableListType()).size());
            Assert.assertTrue(_params.get(_pc.getVariableListType()).contains("_hiddenName"));
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectNoLocalNameHiding()
    {
        try {
            extractRegion(7, 10);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = mdfaAnalysis();
            cc.setClassName("ExtractMethodCases");
            cc.setCompilationUnit(_cu);

            MethodExtractNoNameHiding menh = new MethodExtractNoNameHiding(cc);

            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoNameHiding, menh.getType());
            assertTrue(menh.detect());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void detectReturnStatementInExtractedCode()
    {
        try {
            extractRegion(145, 154);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = new ContextConfiguration();
            cc.setCodeSection(_extractRegion);
            cc.setCompilationUnit(_cu);
            cc.setCMFAnalyzer(_cmf);

            MethodExtractControlReturn mecr = new MethodExtractControlReturn(cc);

            assertTrue(mecr.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractControlReturn, mecr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void no_DetectReturnStatementOutsideExtractedCode()
    {
        try {
            extractRegion(150, 152);
            setupTestClass("ExtractMethodCases", "ExtractionWithoutDependencies");

            ContextConfiguration cc = new ContextConfiguration();
            cc.setCodeSection(_extractRegion);
            cc.setCompilationUnit(_cu);
            cc.setCMFAnalyzer(_cmf);

            MethodExtractControlReturn mecr = new MethodExtractControlReturn(cc);
            MethodExtractNoControlReturn mencr = new MethodExtractNoControlReturn(cc);

            assertFalse(mecr.detect());

            assertTrue(mencr.detect());
            Assert.assertEquals(CodeContext.CodeContextEnum.MethodExtractNoControlReturn, mencr.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}