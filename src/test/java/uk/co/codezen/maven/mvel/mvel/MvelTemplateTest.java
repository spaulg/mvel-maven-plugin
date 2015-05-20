package uk.co.codezen.maven.mvel.mvel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import uk.co.codezen.maven.mvel.mvel.exception.OutputPathInvalidOrNotDefinedException;
import uk.co.codezen.maven.mvel.mvel.exception.TemplatePathInvalidOrNotDefinedException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MvelTemplateTest
{
    private String testOutputPath;
    private Map<String,Object> parameterMap;

    @Before
    public void setUp()
    {
        this.parameterMap = new HashMap<String,Object>();
        this.parameterMap.put("hello_property", "Hello");
        this.parameterMap.put("world_property", "World");

        this.testOutputPath = System.getProperty("project.build.testOutputDirectory");
    }

    @Test(expected = TemplatePathInvalidOrNotDefinedException.class)
    public void renderWithNullTemplatePath()
        throws TemplatePathInvalidOrNotDefinedException, OutputPathInvalidOrNotDefinedException, IOException
    {
        MvelTemplate template = new MvelTemplate();
        template.setTemplatePath(null);
        template.setOutputPath(String.format("%s%stemplate-actual.html", this.testOutputPath, File.separator));

        template.render(this.parameterMap);
    }

    @Test(expected = TemplatePathInvalidOrNotDefinedException.class)
    public void renderWithBlankTemplatePath()
        throws TemplatePathInvalidOrNotDefinedException, OutputPathInvalidOrNotDefinedException, IOException
    {
        MvelTemplate template = new MvelTemplate();
        template.setTemplatePath("");
        template.setOutputPath(String.format("%s%stemplate-actual.html", this.testOutputPath, File.separator));

        template.render(this.parameterMap);
    }

    @Test(expected = OutputPathInvalidOrNotDefinedException.class)
    public void renderWithNullOutputPath()
        throws TemplatePathInvalidOrNotDefinedException, OutputPathInvalidOrNotDefinedException, IOException
    {
        MvelTemplate template = new MvelTemplate();
        template.setTemplatePath(this.getClass().getResource("/template.html").getFile());
        template.setOutputPath(null);

        template.render(this.parameterMap);
    }

    @Test(expected = OutputPathInvalidOrNotDefinedException.class)
    public void renderWithBlankOutputPath()
        throws TemplatePathInvalidOrNotDefinedException, OutputPathInvalidOrNotDefinedException, IOException
    {
        MvelTemplate template = new MvelTemplate();
        template.setTemplatePath(this.getClass().getResource("/template.html").getFile());
        template.setOutputPath("");

        template.render(this.parameterMap);
    }

    @Test
    public void render()
        throws TemplatePathInvalidOrNotDefinedException, OutputPathInvalidOrNotDefinedException, IOException
    {
        MvelTemplate template = new MvelTemplate();
        template.setTemplatePath(this.getClass().getResource("/template.html").getFile());
        template.setOutputPath(String.format("%s%stemplate-actual.html", this.testOutputPath, File.separator));

        template.render(this.parameterMap);

        String expectedContent = this.readFile(this.getClass().getResource("/template-expected.html").getPath());
        String actualContent = this.readFile(this.getClass().getResource("/template-actual.html").getPath());

        assertEquals(expectedContent, actualContent);
    }

    /**
     * Read and return a files contents
     *
     * @param file File to read
     * @return Contents read
     * @throws IOException
     */
    private String readFile(String file)
        throws IOException
    {
        FileInputStream stream = new FileInputStream(new File(file));
        InputStreamReader reader = new InputStreamReader(stream);

        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        char[] buffer = new char[1024];
        while (-1 != (n = reader.read(buffer, 0, 1024))) {
            stringBuilder.append(buffer, 0, n);
        }

        return stringBuilder.toString();
    }
}
