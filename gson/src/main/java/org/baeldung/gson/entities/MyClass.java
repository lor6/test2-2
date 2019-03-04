package org.baeldung.gson.entities;

public class MyClass {
    private int id;
    private String[] strings;

    public MyClass() {
        id = 1;
        strings = new String[] { "a", "b" };
    }


    //  @Override
    //  public String toString() {
    //    return "{" +
    //        "id=" + id +
    //        ", name='" + name + '\'' +
    //        ", strings=" + Arrays.toString(strings) +
    //        '}';
    //  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }
}
