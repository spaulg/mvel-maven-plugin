
# mvel-maven-plugin

A maven plugin for rendering templates with MVEL.

## Usage

```xml
<plugin>
    <groupId>uk.co.codezen</groupId>
    <artifactId>mvel-maven-plugin</artifactId>
    <version>1.0</version>
 
    <executions>
        <execution>
            <goals>
                <goal>render</goal>
            </goals>
        
            <configuration>
                <templates>
                    <template>
                        <templatePath>${basedir}/src/main/webapp/WEB-INF/web.xml</templatePath>
                        <outputPath>${project.build.directory}/${project.build.finalName}/WEB-INF/web.xml</outputPath>
                    </template>
                </templates>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## License

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
