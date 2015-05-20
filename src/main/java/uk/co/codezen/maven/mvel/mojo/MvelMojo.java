
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

package uk.co.codezen.maven.mvel.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import uk.co.codezen.maven.mvel.mvel.MvelTemplate;
import uk.co.codezen.maven.mvel.mvel.exception.OutputPathInvalidOrNotDefinedException;
import uk.co.codezen.maven.mvel.mvel.exception.TemplatePathInvalidOrNotDefinedException;

import java.io.IOException;
import java.util.*;

@Mojo(name = "render", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
final public class MvelMojo extends AbstractMojo
{
    /**
     * List of MVEL template files
     */
    @Parameter
    private List<MvelTemplate> templates = new ArrayList<MvelTemplate>();

    /**
     * Maven project
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project = null;

    /**
     * Get MVEL template list
     *
     * @return MVEL template list
     */
    public List<MvelTemplate> getTemplates()
    {
        return this.templates;
    }

    /**
     * Set MVEL template list
     *
     * @param templates MVEL template list
     */
    public void setTemplates(List<MvelTemplate> templates)
    {
        this.templates = templates;
    }

    /**
     * Get Maven project
     *
     * @return Maven project
     */
    public MavenProject getProject()
    {
        return this.project;
    }

    /**
     * Set Maven project
     *
     * @param project Maven project
     */
    public void setProject(MavenProject project)
    {
        this.project = project;
    }

    /**
     * Execute goal
     *
     * @throws MojoExecutionException There was a problem running the Mojo.
     *          Further details are available in the message and cause properties.
     */
    public void execute() throws MojoExecutionException
    {
        // Create global parameter map
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("project", this.getProject());
        parameterMap.put("env", System.getenv());

        Properties systemProperties = System.getProperties();
        for (String propertyName : systemProperties.stringPropertyNames()) {
            parameterMap.put(propertyName, systemProperties.getProperty(propertyName));
        }

        Properties projectProperties = this.getProject().getProperties();
        for (String propertyName : projectProperties.stringPropertyNames()) {
            parameterMap.put(propertyName, projectProperties.getProperty(propertyName));
        }

        try {
            for (MvelTemplate template : this.templates) {
                template.render(parameterMap);
            }
        }
        catch(IOException ex) {
            throw new MojoExecutionException("Failed to generate MVEL template", ex);
        }
        catch(OutputPathInvalidOrNotDefinedException ex) {
            throw new MojoExecutionException("Failed to generate MVEL template", ex);
        }
        catch (TemplatePathInvalidOrNotDefinedException ex) {
            throw new MojoExecutionException("Failed to generate MVEL template", ex);
        }
    }
}
