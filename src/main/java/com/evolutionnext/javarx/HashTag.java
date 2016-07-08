package com.evolutionnext.javarx;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Daniel Hinojosa
 * @since 7/6/16 12:22 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class HashTag implements Comparable<HashTag> {
    private final String tag;

    public HashTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashTag hashTag = (HashTag) o;
        return Objects.equals(tag, hashTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HashTag(");
        sb.append(tag);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public int compareTo(HashTag o) {
        return this.tag.compareTo(o.getTag());
    }
}
