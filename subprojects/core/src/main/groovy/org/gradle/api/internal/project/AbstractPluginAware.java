/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.project;

import groovy.lang.Closure;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DefaultObjectConfigurationAction;
import org.gradle.api.plugins.PluginAware;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.configuration.ScriptPluginFactory;
import org.gradle.util.ConfigureUtil;

import java.util.Map;

abstract public class AbstractPluginAware implements PluginAware {
    private PluginContainer pluginContainer;
    private ServiceRegistryFactory services;
    private FileResolver fileResolver;

    protected AbstractPluginAware() {
    }

    public AbstractPluginAware(ServiceRegistryFactory serviceRegistryFactory) {
        this.services = serviceRegistryFactory.createFor(this);
    }

    public PluginContainer getPlugins() {
        if (pluginContainer == null) {
            pluginContainer = services.get(PluginContainer.class);
        }
        return pluginContainer;
    }

    public void apply(Closure closure) {
        DefaultObjectConfigurationAction action = new DefaultObjectConfigurationAction(getFileResolver(), services.get(
                ScriptPluginFactory.class), this);
        ConfigureUtil.configure(closure, action);
        action.execute();
    }

    FileResolver getFileResolver() {
        if(fileResolver == null){
            fileResolver = services.get(FileResolver.class);
        }
        return fileResolver;
    }


    public void apply(Map<String, ?> options) {
        DefaultObjectConfigurationAction action = new DefaultObjectConfigurationAction(getFileResolver(), services.get(
                ScriptPluginFactory.class), this);
        ConfigureUtil.configureByMap(options, action);
        action.execute();
    }
}
