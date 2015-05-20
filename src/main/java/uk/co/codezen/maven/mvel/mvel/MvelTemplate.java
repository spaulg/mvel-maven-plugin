
/*
    Copyright 2014 Simon Paulger <spaulger@codezen.co.uk>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package uk.co.codezen.maven.mvel.mvel;

import org.mvel2.templates.TemplateRuntime;
import uk.co.codezen.maven.mvel.mvel.exception.OutputPathInvalidOrNotDefinedException;
import uk.co.codezen.maven.mvel.mvel.exception.TemplatePathInvalidOrNotDefinedException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

final public class MvelTemplate
{
    /**
     * Template path
     */
    private String templatePath;

    /**
     * Output path
     */
    private String outputPath;

    /**
     * Get template path
     *
     * @return Template path
     */
    public String getTemplatePath()
    {
        return this.templatePath;
    }

    /**
     * Set template path
     *
     * @param templatePath Template path
     */
    public void setTemplatePath(String templatePath)
    {
        this.templatePath = templatePath;
    }

    /**
     * Get output path
     *
     * @return Output path
     */
    public String getOutputPath()
    {
        return this.outputPath;
    }

    /**
     * Set output path
     *
     * @param outputPath Output path
     */
    public void setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
    }

    /**
     * Render the template to the destination path
     */
    public void render(Map<String,Object> parameterMap)
        throws IOException, OutputPathInvalidOrNotDefinedException, TemplatePathInvalidOrNotDefinedException
    {
        if (null == this.getOutputPath() || this.getOutputPath().equals("")) {
            // output path not set, refuse to proceed
            throw new OutputPathInvalidOrNotDefinedException();
        }

        if (null == this.getTemplatePath() || this.getTemplatePath().equals("")) {
            throw new TemplatePathInvalidOrNotDefinedException();
        }

        // Ensure the target path exists
        Path outputFilePath = Paths.get(this.getOutputPath());
        Files.createDirectories(outputFilePath.getParent());

        // Read the template file
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        FileReader reader = null;

        try {
            reader = new FileReader(this.getTemplatePath());

            int bytesRead;
            while (-1 != (bytesRead = reader.read(buffer))) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
        }
        finally {
            if (null != reader) {
                reader.close();
            }
        }

        // Parse the template
        String renderedTemplate = (String) TemplateRuntime.eval(stringBuilder.toString(), parameterMap);

        // Write the generated script
        FileWriter writer = null;

        try {
            writer = new FileWriter(outputFilePath.toFile());
            writer.write(renderedTemplate);
        }
        finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}
