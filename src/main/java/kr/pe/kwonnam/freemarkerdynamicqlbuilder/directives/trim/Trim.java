package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim;

import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Trim template result string.
 * <p/>
 * This is thread safe but the setter methods must not be called while calling trim() method.
 */
public class Trim {
    private Logger log = getLogger(Trim.class);

    private String prefix;

    private List<String> prefixOverrides;

    private String suffix;

    private List<String> suffixOverrides;

    public String getPrefix() {
        return prefix;
    }

    /**
     * prefix will be added to the trimmed string's head if the trimmed string is not empty.
     *
     * @param prefix prefix for the result of trim
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getPrefixOverrides() {
        return prefixOverrides;
    }

    /**
     * After trimming, if the trimmed string starts with one of the prefixOverrides,
     * only the first matching item from the head of the trimmed string will be removed.
     *
     * @param prefixOverrides list of strings that will be removed from the head of the trimmed string.
     */
    public void setPrefixOverrides(List<String> prefixOverrides) {
        this.prefixOverrides = prefixOverrides;
    }

    public String getSuffix() {
        return suffix;
    }

    /**
     * suffix will be add to the trimmed string's tail if the trimmed string is not empty.
     *
     * @param suffix suffix for the result of trim
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<String> getSuffixOverrides() {
        return suffixOverrides;
    }

    /**
     * After trimming, if the trimmed string ends with one of the suffixOverrides,
     * only the first matching item from the tail of the trimmed string will be removed.
     *
     * @param suffixOverrides list of strings that will be removed from the tail of the trimmed string.
     */
    public void setSuffixOverrides(List<String> suffixOverrides) {
        this.suffixOverrides = suffixOverrides;
    }

    /**
     * Trim the parameter string and apply configurations.
     *
     * @param str string to be trimmed
     * @return trimmed string. null if str is null.
     */
    public String trim(String str) {
        log.debug("Processing trim prefix : {}, prefixOverrides : {}, suffix : {}, suffixOverrides : {}, str : {}", prefix, prefixOverrides, suffix, suffixOverrides, str);

        if (str == null) {
            return null;
        }

        String trimmed = str.trim();

        if (trimmed.isEmpty()) {
            return trimmed;
        }

        trimmed = processPrefixOverride(trimmed);
        trimmed = processSuffixOverride(trimmed);
        trimmed = processPrefix(trimmed);
        trimmed = processSuffix(trimmed);
        return trimmed;
    }

    private String processPrefixOverride(String trimmed) {
        if (prefixOverrides == null || prefixOverrides.isEmpty()) {
            return trimmed;
        }

        for (String prefixOverride : prefixOverrides) {
            if (trimmed.startsWith(prefixOverride)) {
                return trimmed.substring(prefixOverride.length());
            }
        }
        return trimmed;
    }

    private String processSuffixOverride(String trimmed) {
        if (suffixOverrides == null || suffixOverrides.isEmpty()) {
            return trimmed;
        }

        for (String suffixOverride : suffixOverrides) {
            if (trimmed.endsWith(suffixOverride)) {
                int idx = trimmed.lastIndexOf(suffixOverride);
                return trimmed.substring(0, idx);
            }
        }
        return trimmed;
    }

    private String processPrefix(String trimmed) {
        if (prefix == null) {
            return trimmed;
        }
        return prefix + trimmed;
    }

    private String processSuffix(String trimmed) {
        if (suffix == null) {
            return trimmed;
        }
        return trimmed + suffix;
    }
}