package org.example.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.example.protobuf.proto.Demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ProtoDemo {
    public static void main(String[] args) {
        // 步骤1：通过 消息类的内部类 Builder 类，构造 消息类 的对象
        Demo.Person.Builder personBuilder = Demo.Person.newBuilder();

        // 步骤2：设置消息信息
        personBuilder.setName("eric");
        personBuilder.setId(123);
        personBuilder.setEmail("eric@gmail.com");

        Demo.Person.PhoneNumber.Builder phoneNumberBuilder = Demo.Person.PhoneNumber.newBuilder();
        phoneNumberBuilder.setType(Demo.Person.PhoneType.HOME);
        phoneNumberBuilder.setNumber("0551-1234567");

        // 步骤3：创建 消息类 对象
        Demo.Person person = personBuilder.build();

        // 步骤4：序列化和反序列化消息（两种方式）

        // 方式一：通过字节数组
        // 序列化
        byte[] bytes = person.toByteArray();
        System.out.println("序列化之后的字节数组: " + Arrays.toString(bytes));

        // 反序列化
        try {
            Demo.Person personRequest = Demo.Person.parseFrom(bytes);
            System.out.println("反序列化之后: " + personRequest.getId());
            System.out.println("反序列化之后: " + personRequest.getName());
            System.out.println("反序列化之后: " + personRequest.getEmail());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        System.out.println("=========================");

        // 方式二：通过输入/输出流
        // 序列化
        ByteOutputStream out = new ByteOutputStream();
        try {
            person.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bytes = out.toByteArray();

        // 反序列化
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            Demo.Person personRequest = Demo.Person.parseFrom(in);
            System.out.println("反序列化之后: " + personRequest.getId());
            System.out.println("反序列化之后: " + personRequest.getName());
            System.out.println("反序列化之后: " + personRequest.getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
