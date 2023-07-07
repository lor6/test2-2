package com.baeldung.jackson.defaultvalues;

import com.fasterxml.jackson.annotation.JsonSetter;

public class JsonSetterDefaultValue {

    private String required;
    private String optional = "valueIfMissingEntirely";

    @JsonSetter("optional")
    public void setOptional(String optional){
        if(optional == null){
            this.optional = "valueIfNull";
        }
    }

    public String getRequired() {
        return required;
    }

    public String getOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "NonAnnotatedDefaultValue{" + "required='" + required + '\'' + ", optional='" + optional + '\'' + '}';
    }

}
