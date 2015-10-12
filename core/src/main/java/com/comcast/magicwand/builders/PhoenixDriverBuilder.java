/**
 * Copyright 2015 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comcast.magicwand.builders;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import java.net.URL;
import java.net.MalformedURLException;

import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.wizards.WizardFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.reflections.Reflections;
import org.reflections.util.FilterBuilder;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.scanners.SubTypesScanner;

/**
 * Class that is responsible for defining the order to be used to resolve a {@link PhoenixDriver}
 *
 * <br>
 * Default order is defined by the order in which the matching interfaces are loaded from the class path.
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class PhoenixDriverBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(PhoenixDriverBuilder.class);

    private static final String MAGICWAND_PACKAGE_VAR = "MAGICWAND_PACKAGES";

    private List<WizardFactory> order;
    private PhoenixDriverIngredients ingredients;

    /**
     * Creates an instance of this class
     */
    public PhoenixDriverBuilder() {
        this.order = new ArrayList<WizardFactory>();
    }

    /**
     * Adds Custom resolution type to the list
     *
     * @param wizard Custom wizard factory to use to create a driver
     * @return this builder
     */
    public PhoenixDriverBuilder forCustom(WizardFactory wizard) {
        this.order.add(wizard);

        return this;
    }

    /**
     * Adds a set of ingredients
     *
     * @param ingredients Ingredients to use for driver construction
     * @return this builder
     */
    public PhoenixDriverBuilder withIngredients(PhoenixDriverIngredients ingredients) {
        this.ingredients = ingredients;

        return this;
    }

    private static Collection<URL> getUrlChunks(String urlLine, String source) {
        if(source == null)
            source = "";

        Collection<URL> urls = new HashSet<URL>();

        String[] chunks = urlLine.split(";");

        for(int i = 0; i < chunks.length; i++) {
            if(!chunks[i].trim().equals("")) {
                try {
                    URL you_are_el = new URL(chunks[i].trim());
                    urls.add(you_are_el);
                    LOG.debug("{} URL[{}]: {}", source, i, you_are_el);
                }
                catch(MalformedURLException mue) {
                    LOG.error("ERROR: {}", mue);
                }
            }
        }

        return urls;
    }

    /**
     * Load {@link WizardFactory}'s from the classpath
     *
     * @return List of WizardFactory classes.
     */
    public static final List<WizardFactory> loadAllFactories() {
        List<WizardFactory> factories = new LinkedList<WizardFactory>();

        ClassLoader loader = ClasspathHelper.contextClassLoader();

        Package[] packs = Package.getPackages();

        Collection<URL> urls = new HashSet<URL>();

        urls.addAll(ClasspathHelper.forPackage("com.comcast.magicwand"));
        urls.addAll(ClasspathHelper.forJavaClassPath());
        urls.addAll(ClasspathHelper.forClassLoader());
        for(int i = 0; i < packs.length; i++)
            urls.addAll(ClasspathHelper.forPackage(packs[i].getName(), loader));

        Map<String, String> env = System.getenv();
        if(env.containsKey(MAGICWAND_PACKAGE_VAR))
            urls.addAll(getUrlChunks(env.get(MAGICWAND_PACKAGE_VAR), "Env "));

        String factory_prop = System.getProperty(MAGICWAND_PACKAGE_VAR);
        if(factory_prop != null)
            urls.addAll(getUrlChunks(factory_prop, "Prop"));

        ConfigurationBuilder conf = new ConfigurationBuilder();
        conf.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.comcast.magicwand")));
        conf.setUrls(urls);
        conf.setScanners(new SubTypesScanner());

        Reflections ref = new Reflections(conf);

        Set<Class<? extends WizardFactory>> fact_set = ref.getSubTypesOf(WizardFactory.class);

        Iterator<Class<? extends WizardFactory>> iter = fact_set.iterator();

        while(iter.hasNext()) {
            try {
                factories.add(iter.next().newInstance());
            }
            catch(InstantiationException | IllegalAccessException ie) {
                LOG.error("ERROR: {}", ie);
            }
        }

        return factories;
    }

    /**
     * Creates the order of wizard resolution
     *
     * @return List of wizards to use to create drivers
     */
    protected List<WizardFactory> generateDefaultLookupOrderIfNeeded() {
        // if no ranks have been added, specify default order
        if (order.isEmpty()) {
            this.order.addAll(PhoenixDriverBuilder.loadAllFactories());
        }

        return this.order;
    }

    /**
     * Creates a {@link PhoenixDriver} using this builder's arguments
     *
     * @return Instance of a {@link PhoenixDriver} to use for testing
     */
    public PhoenixDriver build() {
        PhoenixDriver rv = null;

        generateDefaultLookupOrderIfNeeded();
        PhoenixDriverIngredients curIngredients;

        curIngredients = getVerifiedIngredients();

        if (null == curIngredients) {
            return null;
        }

        // TODO: How should Dawg be handled if mobile OS is not set? who should handle it?

        for (WizardFactory wizardFactory : this.order) {
            LOG.debug("Trying to create driver using '" + wizardFactory.getWizardFactoryName() + "' wizard");
            try {
                rv = wizardFactory.create(curIngredients);
                if (null != rv) {
                    break;
                }
            }
            catch (Throwable e) {
                // Swallow the exception and try another wizard
                String message = "There was an error while trying to create a driver using '"
                        + wizardFactory.getWizardFactoryName() + "'";
                LOG.error(message, e);
            }
        }

        return rv;
    }

    /**
     * Verifies driver ingredients
     *
     * @return Verified ingredients or null if validation failed
     */
    public PhoenixDriverIngredients getVerifiedIngredients() {
        if (null == this.ingredients) {
            this.ingredients = new PhoenixDriverIngredients();
        }

        return this.ingredients.verify();
    }
}
