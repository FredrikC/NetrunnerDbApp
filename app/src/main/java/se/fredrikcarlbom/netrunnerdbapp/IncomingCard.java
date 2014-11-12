package se.fredrikcarlbom.netrunnerdbapp;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IncomingCard {
    @JsonProperty("code")
    public int Code;

    @JsonProperty("title")
    public String Title;

    /**
     * The textural representation of the type
     */
    @JsonProperty("type")
    public String Type;

    /**
     * The case agnostic representation of the type, should be parsed as enum
     */
    @JsonProperty("type_code")
    public String TypeCode;

    @JsonProperty("imagesrc")
    public String ImageSrc;

    public Bitmap Bitmap;
}
