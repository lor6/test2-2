package org.baeldung.gson.advance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.baeldung.gson.entities.Animal;
import org.baeldung.gson.entities.Cow;
import org.baeldung.gson.entities.Dog;
import org.baeldung.gson.entities.MyClass;
import org.baeldung.gson.serialization.AnimalDeserializer;
import org.junit.Test;

public class GsonAdvanceUnitTest {

    @Test public void givenListOfMyClass_whenSerializing_thenCorrect() {
        List<MyClass> list = new ArrayList<>();
        list.add(new MyClass());
        list.add(new MyClass());

        Gson gson = new Gson();
        String jsonString = gson.toJson(list);
        String expectedString = "[{\"id\":1,\"strings\":[\"a\",\"b\"]},{\"id\":1,\"strings\":[\"a\",\"b\"]}]";

        assertEquals(expectedString, jsonString);
    }

    @Test(expected = ClassCastException.class)
    public void givenJsonString_whenIncorrectDeserializing_thenThrowClassCastException() {
        String inputString = "[{\"id\":1,\"strings\":[\"a\",\"b\"]},{\"id\":1,\"strings\":[\"a\",\"b\"]}]";

        Gson gson = new Gson();
        List<MyClass> list = gson.fromJson(inputString, ArrayList.class);

        assertEquals(2, list.size());
        int id = list.get(0).getId();
    }

    @Test public void givenJsonString_whenDeserializing_thenReturnListOfMyClass() {
        String inputString = "[{\"id\":1,\"strings\":[\"a\",\"b\"]},{\"id\":1,\"strings\":[\"a\",\"b\"]}]";
        Type listOfMyClassObject = new TypeToken<ArrayList<MyClass>>() {}.getType();

        Gson gson = new Gson();
        List<MyClass> list = gson.fromJson(inputString, listOfMyClassObject);

        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
    }

    @Test public void givenPolymorphicList_whenSerializeWithTypeAdapter_thenCorrect() {
        String expectedString = "[{\"petName\":\"Milo\",\"type\":\"Dog\"},{\"breed\":\"Jersey\",\"type\":\"Cow\"}]";

        List<Animal> inList = new ArrayList<>();
        inList.add(new Dog());
        inList.add(new Cow());

        String jsonString = new Gson().toJson(inList);

        assertEquals(expectedString, jsonString);
    }

    @Test public void givenPolymorphicList_whenDeserializeWithTypeAdapter_thenCorrect() {
        String inputString = "[{\"petName\":\"Milo\",\"type\":\"Dog\"},{\"breed\":\"Jersey\",\"type\":\"Cow\"}]";

        AnimalDeserializer deserializer = new AnimalDeserializer("type");
        deserializer.registerBarnType("Dog", Dog.class);
        deserializer.registerBarnType("Cow", Cow.class);
        Gson gson = new GsonBuilder()
          .registerTypeAdapter(Animal.class, deserializer)
          .create();

        List<Animal> outList = gson.fromJson(inputString, new TypeToken<List<Animal>>(){}.getType());

        assertEquals(2, outList.size());
        assertTrue(outList.get(0) instanceof Dog);
    }

    @Test public void givenPolymorphicList_whenSerializeWithRuntimeTypeAdapter_thenCorrect() {
        String expectedString = "[{\"petName\":\"Milo\",\"type\":\"Dog\"},{\"breed\":\"Jersey\",\"type\":\"Cow\"}]";

        List<Animal> inList = new ArrayList<>();
        inList.add(new Dog());
        inList.add(new Cow());
        String jsonString = new Gson().toJson(inList);

        assertEquals(expectedString, jsonString);
    }

    @Test public void givenPolymorphicList_whenDeserializeWithRuntimeTypeAdapter_thenCorrect() {
        String inputString = "[{\"petName\":\"Milo\",\"type\":\"Dog\"},{\"breed\":\"Jersey\",\"type\":\"Cow\"}]";

        Type listOfAnimals = new TypeToken<ArrayList<Animal>>() {}.getType();

        RuntimeTypeAdapterFactory<Animal> adapter = RuntimeTypeAdapterFactory.of(Animal.class)
          .registerSubtype(Dog.class)
          .registerSubtype(Cow.class);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

        List<Animal> outList = gson.fromJson(inputString, listOfAnimals);

        assertEquals(2, outList.size());
        assertTrue(outList.get(0) instanceof Dog);
    }
}