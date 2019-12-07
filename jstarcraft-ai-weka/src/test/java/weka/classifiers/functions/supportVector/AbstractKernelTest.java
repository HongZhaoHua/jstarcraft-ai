/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2006 University of Waikato 
 */

package weka.classifiers.functions.supportVector;

import junit.framework.TestCase;
import weka.core.Attribute;
import weka.core.CheckGOE;
import weka.core.CheckOptionHandler;
import weka.core.CheckScheme.PostProcessor;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.test.Regression;

/**
 * Abstract Test class for Kernels. Internally it uses the class
 * <code>CheckKernel</code> to determine success or failure of the tests. It
 * follows basically the <code>testsPerClassType</code> method.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 *
 * @see CheckKernel
 * @see CheckKernel#testsPerClassType(int, boolean, boolean)
 * @see PostProcessor
 */
public abstract class AbstractKernelTest extends TestCase {

    /** The Kernel to be tested */
    protected Kernel m_Kernel;

    /** For testing the Kernel */
    protected CheckKernel m_Tester;

    /** whether Kernel handles weighted instances */
    protected boolean m_weightedInstancesHandler;

    /** whether Kernel handles multi-instance data */
    protected boolean m_multiInstanceHandler;

    /**
     * the number of classes to test with testNClasses()
     * 
     * @see #testNClasses()
     */
    protected int m_NClasses;

    /** whether to run CheckKernel in DEBUG mode */
    protected boolean DEBUG = false;

    /** the attribute type with the lowest value */
    protected final static int FIRST_CLASSTYPE = Attribute.NUMERIC;

    /** the attribute type with the highest value */
    protected final static int LAST_CLASSTYPE = Attribute.RELATIONAL;

    /**
     * wether Kernel can predict nominal attributes (array index is attribute type
     * of class)
     */
    protected boolean[] m_NominalPredictors;

    /**
     * wether Kernel can predict numeric attributes (array index is attribute type
     * of class)
     */
    protected boolean[] m_NumericPredictors;

    /**
     * wether Kernel can predict string attributes (array index is attribute type of
     * class)
     */
    protected boolean[] m_StringPredictors;

    /**
     * wether Kernel can predict date attributes (array index is attribute type of
     * class)
     */
    protected boolean[] m_DatePredictors;

    /**
     * wether Kernel can predict relational attributes (array index is attribute
     * type of class)
     */
    protected boolean[] m_RelationalPredictors;

    /** whether Kernel handles missing values */
    protected boolean[] m_handleMissingPredictors;

    /** whether Kernel handles class with only missing values */
    protected boolean[] m_handleMissingClass;

    /** whether Kernel handles class as first attribute */
    protected boolean[] m_handleClassAsFirstAttribute;

    /** whether Kernel handles class as second attribute */
    protected boolean[] m_handleClassAsSecondAttribute;

    /** the results of the regression tests */
    protected String[] m_RegressionResults;

    /** the OptionHandler tester */
    protected CheckOptionHandler m_OptionTester;

    /** for testing GOE stuff */
    protected CheckGOE m_GOETester;

    /**
     * Constructs the <code>AbstractKernelTest</code>. Called by subclasses.
     *
     * @param name the name of the test class
     */
    public AbstractKernelTest(String name) {
        super(name);
    }

    /**
     * returns a custom PostProcessor for the CheckKernel datasets, currently only
     * null.
     * 
     * @return a custom PostProcessor, if necessary
     * @see PostProcessor
     */
    protected PostProcessor getPostProcessor() {
        return null;
    }

    /**
     * configures the CheckKernel instance used throughout the tests
     * 
     * @return the fully configured CheckKernel instance used for testing
     */
    protected CheckKernel getTester() {
        CheckKernel result;

        result = new CheckKernel();
        result.setSilent(true);
        result.setKernel(m_Kernel);
        result.setNumInstances(20);
        result.setDebug(DEBUG);
        result.setPostProcessor(getPostProcessor());

        return result;
    }

    /**
     * Configures the CheckOptionHandler uses for testing the optionhandling. Sets
     * the Kernel return from the getKernel() method.
     * 
     * @return the fully configured CheckOptionHandler
     * @see #getKernel()
     */
    protected CheckOptionHandler getOptionTester() {
        CheckOptionHandler result;

        result = new CheckOptionHandler();
        if (getKernel() instanceof OptionHandler)
            result.setOptionHandler((OptionHandler) getKernel());
        else
            result.setOptionHandler(null);
        result.setUserOptions(new String[0]);
        result.setSilent(true);

        return result;
    }

    /**
     * Configures the CheckGOE used for testing GOE stuff. Sets the Kernel returned
     * from the getKernel() method.
     * 
     * @return the fully configured CheckGOE
     * @see #getKernel()
     */
    protected CheckGOE getGOETester() {
        CheckGOE result;

        result = new CheckGOE();
        result.setObject(getKernel());
        result.setSilent(true);

        return result;
    }

    /**
     * Called by JUnit before each test method. This implementation creates the
     * default Kernel to test and loads a test set of Instances.
     *
     * @exception Exception if an error occurs reading the example instances.
     */
    protected void setUp() throws Exception {
        m_Kernel = getKernel();
        m_Tester = getTester();
        m_OptionTester = getOptionTester();
        m_GOETester = getGOETester();

        m_weightedInstancesHandler = m_Tester.weightedInstancesHandler()[0];
        m_multiInstanceHandler = m_Tester.multiInstanceHandler()[0];
        m_NominalPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_NumericPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_StringPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_DatePredictors = new boolean[LAST_CLASSTYPE + 1];
        m_RelationalPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_handleMissingPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_handleMissingClass = new boolean[LAST_CLASSTYPE + 1];
        m_handleClassAsFirstAttribute = new boolean[LAST_CLASSTYPE + 1];
        m_handleClassAsSecondAttribute = new boolean[LAST_CLASSTYPE + 1];
        m_RegressionResults = new String[LAST_CLASSTYPE + 1];
        m_NClasses = 4;

        // initialize attributes
        checkAttributes(true, false, false, false, false, false);
        checkAttributes(false, true, false, false, false, false);
        checkAttributes(false, false, true, false, false, false);
        checkAttributes(false, false, false, true, false, false);
        checkAttributes(false, false, false, false, true, false);

        // initialize missing values handling
        for (int i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the scheme support this type of class at all?
            if (!canPredict(i))
                continue;

            // 20% missing
            m_handleMissingPredictors[i] = checkMissingPredictors(i, 20, false);
            m_handleMissingClass[i] = checkMissingClass(i, 20, false);
        }
    }

    /** Called by JUnit after each test method */
    protected void tearDown() {
        m_Kernel = null;
        m_Tester = null;
        m_OptionTester = null;
        m_GOETester = null;

        m_weightedInstancesHandler = false;
        m_NominalPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_NumericPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_StringPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_DatePredictors = new boolean[LAST_CLASSTYPE + 1];
        m_RelationalPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_handleMissingPredictors = new boolean[LAST_CLASSTYPE + 1];
        m_handleMissingClass = new boolean[LAST_CLASSTYPE + 1];
        m_handleClassAsFirstAttribute = new boolean[LAST_CLASSTYPE + 1];
        m_handleClassAsSecondAttribute = new boolean[LAST_CLASSTYPE + 1];
        m_RegressionResults = new String[LAST_CLASSTYPE + 1];
        m_NClasses = 4;
    }

    /**
     * Used to create an instance of a specific Kernel.
     *
     * @return a suitably configured <code>Kernel</code> value
     */
    public abstract Kernel getKernel();

    /**
     * checks whether at least one attribute type can be handled with the given
     * class type
     *
     * @param type the class type to check for
     * @return true if at least one attribute type can be predicted with the given
     *         class
     */
    protected boolean canPredict(int type) {
        return m_NominalPredictors[type] || m_NumericPredictors[type] || m_StringPredictors[type] || m_DatePredictors[type] || m_RelationalPredictors[type];
    }

    /**
     * returns a string for the class type
     * 
     * @param type the class type
     * @return the class type as string
     */
    protected String getClassTypeString(int type) {
        return CheckKernel.attributeTypeToString(type);
    }

    /**
     * tests whether the Kernel can handle certain attributes and if not, if the
     * exception is OK
     *
     * @param nom       to check for nominal attributes
     * @param num       to check for numeric attributes
     * @param str       to check for string attributes
     * @param dat       to check for date attributes
     * @param rel       to check for relational attributes
     * @param allowFail whether a junit fail can be executed
     * @see CheckKernel#canPredict(boolean, boolean, boolean, boolean, boolean,
     *      boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    protected void checkAttributes(boolean nom, boolean num, boolean str, boolean dat, boolean rel, boolean allowFail) {
        boolean[] result;
        String att;
        int i;

        // determine text for type of attributes
        att = "";
        if (nom)
            att = "nominal";
        else if (num)
            att = "numeric";
        else if (str)
            att = "string";
        else if (dat)
            att = "date";
        else if (rel)
            att = "relational";

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            result = m_Tester.canPredict(nom, num, str, dat, rel, m_multiInstanceHandler, i);
            if (nom)
                m_NominalPredictors[i] = result[0];
            else if (num)
                m_NumericPredictors[i] = result[0];
            else if (str)
                m_StringPredictors[i] = result[0];
            else if (dat)
                m_DatePredictors[i] = result[0];
            else if (rel)
                m_RelationalPredictors[i] = result[0];

            if (!result[0] && !result[1] && allowFail)
                fail("Error handling " + att + " attributes (" + getClassTypeString(i) + " class)!");
        }
    }

    /**
     * tests whether the Kernel can handle different types of attributes and if not,
     * if the exception is OK
     *
     * @see #checkAttributes(boolean, boolean, boolean, boolean, boolean, boolean)
     */
    public void testAttributes() {
        // nominal
        checkAttributes(true, false, false, false, false, true);
        // numeric
        checkAttributes(false, true, false, false, false, true);
        // string
        checkAttributes(false, false, true, false, false, true);
        // date
        checkAttributes(false, false, false, true, false, true);
        // relational
        if (!m_multiInstanceHandler)
            checkAttributes(false, false, false, false, true, true);
    }

    /**
     * tests whether the scheme declares a serialVersionUID.
     */
    public void testSerialVersionUID() {
        boolean[] result;

        result = m_Tester.declaresSerialVersionUID();

        if (!result[0])
            fail("Doesn't declare serialVersionUID!");
    }

    /**
     * tests whether the Kernel handles instance weights correctly
     *
     * @see CheckKernel#instanceWeights(boolean, boolean, boolean, boolean, boolean,
     *      boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testInstanceWeights() {
        boolean[] result;
        int i;

        if (m_weightedInstancesHandler) {
            for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
                // does the Kernel support this type of class at all?
                if (!canPredict(i))
                    continue;

                result = m_Tester.instanceWeights(m_NominalPredictors[i], m_NumericPredictors[i], m_StringPredictors[i], m_DatePredictors[i], m_RelationalPredictors[i], m_multiInstanceHandler, i);

                if (!result[0])
                    System.err.println("Error handling instance weights (" + getClassTypeString(i) + " class)!");
            }
        }
    }

    /**
     * tests whether Kernel handles N classes
     *
     * @see CheckKernel#canHandleNClasses(boolean, boolean, boolean, boolean,
     *      boolean, boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     * @see #m_NClasses
     */
    public void testNClasses() {
        boolean[] result;

        if (!canPredict(Attribute.NOMINAL))
            return;

        result = m_Tester.canHandleNClasses(m_NominalPredictors[Attribute.NOMINAL], m_NumericPredictors[Attribute.NOMINAL], m_StringPredictors[Attribute.NOMINAL], m_DatePredictors[Attribute.NOMINAL], m_RelationalPredictors[Attribute.NOMINAL], m_multiInstanceHandler, m_NClasses);

        if (!result[0] && !result[1])
            fail("Error handling " + m_NClasses + " classes!");
    }

    /**
     * checks whether the Kernel can handle the class attribute at a given position
     * (0-based index, -1 means last).
     *
     * @param type     the class type
     * @param position the position of the class attribute (0-based, -1 means last)
     * @return true if the Kernel can handle it
     */
    protected boolean checkClassAsNthAttribute(int type, int position) {
        boolean[] result;
        String indexStr;

        result = m_Tester.canHandleClassAsNthAttribute(m_NominalPredictors[type], m_NumericPredictors[type], m_StringPredictors[type], m_DatePredictors[type], m_RelationalPredictors[type], m_multiInstanceHandler, type, position);

        if (position == -1)
            indexStr = "last";
        else
            indexStr = (position + 1) + ".";

        if (!result[0] && !result[1])
            fail("Error handling class as " + indexStr + " attribute (" + getClassTypeString(type) + " class)!");

        return result[0];
    }

    /**
     * Tests whether the Kernel can handle class attributes as Nth attribute. In
     * case of multi-instance Kernels it performs no tests, since the multi-instance
     * data has a fixed format (bagID,bag,class).
     *
     * @see CheckKernel#canHandleClassAsNthAttribute(boolean, boolean, boolean,
     *      boolean, boolean, boolean, int, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testClassAsNthAttribute() {
        int i;

        // multi-Instance data has fixed format!
        if (m_multiInstanceHandler)
            return;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            // first attribute
            m_handleClassAsFirstAttribute[i] = checkClassAsNthAttribute(i, 0);

            // second attribute
            m_handleClassAsSecondAttribute[i] = checkClassAsNthAttribute(i, 1);
        }
    }

    /**
     * tests whether the Kernel can handle zero training instances
     *
     * @see CheckKernel#canHandleZeroTraining(boolean, boolean, boolean, boolean,
     *      boolean, boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testZeroTraining() {
        boolean[] result;
        int i;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            result = m_Tester.canHandleZeroTraining(m_NominalPredictors[i], m_NumericPredictors[i], m_StringPredictors[i], m_DatePredictors[i], m_RelationalPredictors[i], m_multiInstanceHandler, i);

            if (!result[0] && !result[1])
                fail("Error handling zero training instances (" + getClassTypeString(i) + " class)!");
        }
    }

    /**
     * checks whether the Kernel can handle the given percentage of missing
     * predictors
     *
     * @param type      the class type
     * @param percent   the percentage of missing predictors
     * @param allowFail if true a fail statement may be executed
     * @return true if the Kernel can handle it
     */
    protected boolean checkMissingPredictors(int type, int percent, boolean allowFail) {
        boolean[] result;

        result = m_Tester.canHandleMissing(m_NominalPredictors[type], m_NumericPredictors[type], m_StringPredictors[type], m_DatePredictors[type], m_RelationalPredictors[type], m_multiInstanceHandler, type, true, false, percent);

        if (allowFail) {
            if (!result[0] && !result[1])
                fail("Error handling " + percent + "% missing predictors (" + getClassTypeString(type) + " class)!");
        }

        return result[0];
    }

    /**
     * tests whether the Kernel can handle missing predictors (20% and 100%)
     *
     * @see CheckKernel#canHandleMissing(boolean, boolean, boolean, boolean,
     *      boolean, boolean, int, boolean, boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testMissingPredictors() {
        int i;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            // 20% missing
            checkMissingPredictors(i, 20, true);

            // 100% missing
            if (m_handleMissingPredictors[i])
                checkMissingPredictors(i, 100, true);
        }
    }

    /**
     * checks whether the Kernel can handle the given percentage of missing class
     * labels
     *
     * @param type      the class type
     * @param percent   the percentage of missing class labels
     * @param allowFail if true a fail statement may be executed
     * @return true if the Kernel can handle it
     */
    protected boolean checkMissingClass(int type, int percent, boolean allowFail) {
        boolean[] result;

        result = m_Tester.canHandleMissing(m_NominalPredictors[type], m_NumericPredictors[type], m_StringPredictors[type], m_DatePredictors[type], m_RelationalPredictors[type], m_multiInstanceHandler, type, false, true, percent);

        if (allowFail) {
            if (!result[0] && !result[1])
                fail("Error handling " + percent + "% missing class labels (" + getClassTypeString(type) + " class)!");
        }

        return result[0];
    }

    /**
     * tests whether the Kernel can handle missing class values (20% and 100%)
     *
     * @see CheckKernel#canHandleMissing(boolean, boolean, boolean, boolean,
     *      boolean, boolean, int, boolean, boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testMissingClass() {
        int i;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            // 20% missing
            checkMissingClass(i, 20, true);

            // 100% missing
            if (m_handleMissingClass[i])
                checkMissingClass(i, 100, true);
        }
    }

    /**
     * tests whether the Kernel correctly initializes in the buildKernel method
     *
     * @see CheckKernel#correctBuildInitialisation(boolean, boolean, boolean,
     *      boolean, boolean, boolean, int)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testBuildInitialization() {
        boolean[] result;
        int i;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            result = m_Tester.correctBuildInitialisation(m_NominalPredictors[i], m_NumericPredictors[i], m_StringPredictors[i], m_DatePredictors[i], m_RelationalPredictors[i], m_multiInstanceHandler, i);

            if (!result[0] && !result[1])
                fail("Incorrect build initialization (" + getClassTypeString(i) + " class)!");
        }
    }

    /**
     * tests whether the Kernel alters the training set during training.
     *
     * @see CheckKernel#datasetIntegrity(boolean, boolean, boolean, boolean,
     *      boolean, boolean, int, boolean, boolean)
     * @see CheckKernel#testsPerClassType(int, boolean, boolean)
     */
    public void testDatasetIntegrity() {
        boolean[] result;
        int i;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            result = m_Tester.datasetIntegrity(m_NominalPredictors[i], m_NumericPredictors[i], m_StringPredictors[i], m_DatePredictors[i], m_RelationalPredictors[i], m_multiInstanceHandler, i, m_handleMissingPredictors[i], m_handleMissingClass[i]);

            if (!result[0] && !result[1])
                fail("Training set is altered during training (" + getClassTypeString(i) + " class)!");
        }
    }

    /**
     * Builds a model using the current Kernel using the given data and returns the
     * produced output.
     *
     * @param data the instances to test the Kernel on
     * @return a String containing the output of the Kernel.
     */
    protected String useKernel(Instances data) throws Exception {
        Kernel kernel = null;
        StringBuffer text = new StringBuffer();

        try {
            kernel = Kernel.makeCopy(m_Kernel);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problem setting up to use Kernel: " + e);
        }

        kernel.getCapabilities().testWithFail(data);
        kernel.buildKernel(data);
        for (int n = 0; n < data.numInstances(); n++) {
            for (int i = n; i < data.numInstances(); i++) {
                text.append((n + 1) + "-" + (i + 1) + ": " + kernel.eval(n, i, data.instance(i)) + "\n");
            }
        }

        return text.toString();
    }

    /**
     * Provides a hook for derived classes to further modify the data. Currently,
     * the data is just passed through.
     * 
     * @param data the data to process
     * @return the processed data
     */
    protected Instances process(Instances data) {
        return data;
    }

    /**
     * Runs a regression test -- this checks that the output of the tested object
     * matches that in a reference version. When this test is run without any
     * pre-existing reference output, the reference version is created.
     */
    public void testRegression() throws Exception {
        int i;
        boolean succeeded;
        Regression reg;
        Instances train;

        // don't bother if not working correctly
        if (m_Tester.hasClasspathProblems())
            return;

        reg = new Regression(this.getClass());
        succeeded = false;
        train = null;

        for (i = FIRST_CLASSTYPE; i <= LAST_CLASSTYPE; i++) {
            // does the Kernel support this type of class at all?
            if (!canPredict(i))
                continue;

            train = m_Tester.makeTestDataset(42, m_Tester.getNumInstances(), m_NominalPredictors[i] ? 2 : 0, m_NumericPredictors[i] ? 1 : 0, m_StringPredictors[i] ? 1 : 0, m_DatePredictors[i] ? 1 : 0, m_RelationalPredictors[i] ? 1 : 0, 2, i, m_multiInstanceHandler);

            try {
                m_RegressionResults[i] = CheckKernel.attributeTypeToString(i) + " class:\n" + useKernel(train);
                succeeded = true;
                reg.println(m_RegressionResults[i]);
            } catch (Exception e) {
                String msg = e.getMessage().toLowerCase();
                if (msg.indexOf("not in classpath") > -1)
                    return;

                m_RegressionResults[i] = null;
            }
        }

        if (!succeeded) {
            fail("Problem during regression testing: no successful predictions for any class type");
        }

        try {
            String diff = reg.diff();
            if (diff == null) {
                System.err.println("Warning: No reference available, creating.");
            } else if (!diff.equals("")) {
                fail("Regression test failed. Difference:\n" + diff);
            }
        } catch (java.io.IOException ex) {
            fail("Problem during regression testing.\n" + ex);
        }
    }

    /**
     * tests the listing of the options
     */
    public void testListOptions() {
        if (m_OptionTester.getOptionHandler() != null) {
            if (!m_OptionTester.checkListOptions())
                fail("Options cannot be listed via listOptions.");
        }
    }

    /**
     * tests the setting of the options
     */
    public void testSetOptions() {
        if (m_OptionTester.getOptionHandler() != null) {
            if (!m_OptionTester.checkSetOptions())
                fail("setOptions method failed.");
        }
    }

    /**
     * tests whether there are any remaining options
     */
    public void testRemainingOptions() {
        if (m_OptionTester.getOptionHandler() != null) {
            if (!m_OptionTester.checkRemainingOptions())
                fail("There were 'left-over' options.");
        }
    }

    /**
     * tests the whether the user-supplied options stay the same after setting.
     * getting, and re-setting again.
     * 
     * @see #getOptionTester()
     */
    public void testCanonicalUserOptions() {
        if (m_OptionTester.getOptionHandler() != null) {
            if (!m_OptionTester.checkCanonicalUserOptions())
                fail("setOptions method failed");
        }
    }

    /**
     * tests the resetting of the options to the default ones
     */
    public void testResettingOptions() {
        if (m_OptionTester.getOptionHandler() != null) {
            if (!m_OptionTester.checkSetOptions())
                fail("Resetting of options failed");
        }
    }

    /**
     * tests for a globalInfo method
     */
    public void testGlobalInfo() {
        if (!m_GOETester.checkGlobalInfo())
            fail("No globalInfo method");
    }

}
