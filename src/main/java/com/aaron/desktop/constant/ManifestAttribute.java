/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.constant;

import static org.apache.commons.lang3.StringUtils.*;

import com.aaron.desktop.main.Main;
import java.util.EnumMap;

/**
 *
 * @author Aaron
 */
public enum ManifestAttribute
{
    Specification_Title("Vocabulary"),
    Specification_Version("7"),
    Specification_Vendor("Aaron"),
    Implementation_Title("Vocabulary"),
    /*
     git log --oneline | wc -l | gawk '{print $1}'
     git rev-parse --short HEAD
     git rev-parse --abbrev-ref HEAD
     */
    Implementation_Version("35-358c213-master"),
    Implementation_Vendor("Aaron");

    private static final EnumMap<ManifestAttribute, String> APPLICATION_INFO_MAP = initializeApplicationInfoMap();
    private String defaultValue;

    private ManifestAttribute(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    private static EnumMap<ManifestAttribute, String> initializeApplicationInfoMap()
    {
        EnumMap<ManifestAttribute, String> infoMap = new EnumMap<>(ManifestAttribute.class);
        // Navigate from its class object to a package object
        Package objPackage = Main.class.getPackage();

        String title = objPackage.getSpecificationTitle();
        String version = objPackage.getSpecificationVersion();
        String buildVersion = objPackage.getImplementationVersion();
        String author = objPackage.getImplementationVendor();

        if(isNotBlank(title))
        {
            infoMap.put(Specification_Title, title);
        }

        if(isNotBlank(version))
        {
            infoMap.put(Specification_Version, version);
        }

        if(isNotBlank(buildVersion))
        {
            infoMap.put(Implementation_Version, buildVersion);
        }

        if(isNotBlank(author))
        {
            infoMap.put(Implementation_Vendor, author);
        }
        
        return infoMap;
    }
    
    /**
     * Returns the manifest info about the application.
     * @param attribute the manifest attribute to get
     * @return String
     */
    public static String getInfo(ManifestAttribute attribute)
    {
        return APPLICATION_INFO_MAP.getOrDefault(attribute, attribute.getDefaultValue());
    }
}