package org.example.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

public class SpiTest {

    public static void main(String[] args) {

        // ServiceLoader<Car> cars = ServiceLoader.load(Car.class);
        // for (Car car : cars) {
        //     System.out.println(car.getCarName(null));
        // }

        // ExtensionLoader<Protocol> extensionLoader = ExtensionLoader.getExtensionLoader(Protocol.class);
        // Protocol protocol = extensionLoader.getExtension("http");
        // System.out.println(protocol);

        // ExtensionLoader<Car> extensionLoader = ExtensionLoader.getExtensionLoader(Car.class);
        // // Car car = extensionLoader.getExtension("true");
        // Car car = extensionLoader.getExtension("red");
        // System.out.println(car + " = " + car.getCarName(null));

        ExtensionLoader<Person> extensionLoader = ExtensionLoader.getExtensionLoader(Person.class);
        Person person = extensionLoader.getExtension("black");
        System.out.println(person);
        System.out.println(person.getCar());
        URL url = new URL("x", "localhost", 8080);
        url = url.addParameter("car", "black");
        System.out.println(person.getCar().getCarName(url));


        // ExtensionLoader<Filter> extensionLoader = ExtensionLoader.getExtensionLoader(Filter.class);
        // URL url = new URL("http://", "localhost", 8080);
        // url = url.addParameter("cache", "test");
        // List<Filter> activateExtensions = extensionLoader.getActivateExtension(url, new String[]{"validation"}, CommonConstants.CONSUMER);
        // for (Filter activateExtension : activateExtensions) {
        //     System.out.println(activateExtension);
        // }

    }
}
