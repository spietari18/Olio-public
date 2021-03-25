package com.example.w11;

public class SettingsData {
    private Integer font;
    private Integer height;
    private Integer width;
    private Boolean caps;
    private Boolean edit;
    private String text;
    private String displayText;
    private String lang;
    private static SettingsData instance = new SettingsData();

    public SettingsData() {
        font = 10;
        height = 50;
        width = 50;
        caps = false;
        edit = false;
        text = "";
        displayText = "";
        lang = "English";
    }

    public static SettingsData getInstance() {
        return instance;
    }

    public Integer getFont() {
        return font;
    }

    public void setFont(Integer font) {
        this.font = font;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getCaps() {
        return caps;
    }

    public void setCaps(Boolean caps) {
        this.caps = caps;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public void setText(String text) { this.text = text; }

    public String getText() { return text; }

    public void setDisplayText(String text) { this.displayText = text; }

    public String getDisplayText() { return displayText; }

    public void setLang(String lang) { this.lang = lang; }

    public String getLang () { return lang; }
}
