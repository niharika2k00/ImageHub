**TODO**

**Doubts**

- how to implement try catch throw err in java exception -
  ControllerAdvice https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
  HttpStatus.

requestMatchers -> method for configuring path-based access control

Jackson

1 authentication filters
2 authorization filters

    "password": "hello123",

increases throughput

Kafka CAN hold data for an infinite period if you want. But it's not made for '
querying' data, instead, it holds the data temporarily so that the producer api
don't need to wait for the consumer api to 'read' this data, it can simply dump
the data onto the queue and let consumer api read whenever it finds time for it.

multiple kafka server or broker - CLUSTER

4. Store Resized Images
   Store the resized images in a storage service or return them via another
   Kafka topic.
5. Post-processing
   Optionally notify the user or update the database with the resized image URLs
   or metadata.
6.


- CLUSTERED INDEX
-

If you use the @OneToMany annotation with @JoinColumn, then you have a
unidirectional association

Explanation of Changes:
Bidirectional Relationship:

The Image entity uses mappedBy to indicate that the ImageVariant entity owns the
relationship.
Correct @JoinColumn:

The ImageVariant entity correctly maps the foreign key image_id to the primary
key id in the Image entity.
Composite Key:

The ImageVariantId class ensures the composite key (imageId, width, height) is
handled correctly.

Changes:

- Removed @JoinColumn in Image and replaced it with mappedBy = "image" to
  establish bidirectional mapping.
- The ImageVariant entity should now control the foreign key relationship.

Class1 var1 = new Class2();
means that var1 is a reference variable of type Class1, but it is pointing to an
object of type Class2.
This is possible because Class2 is a subclass (or implements an interface) of
Class1. It represents the concept of polymorphism in Java.

class Animal {
void makeSound() {
System.out.println("Some generic sound");
}
}

class Dog extends Animal {
@Override
void makeSound() {
System.out.println("Bark");
}
}

public class Main {
public static void main(String[] args) {
Animal var1 = new Dog(); // Reference type is Animal, object type is Dog
var1.makeSound(); // Output: Bark (Dog's implementation is used)
}
}

When var1.makeSound() is called, the Dog class's implementation of makeSound()
is executed, not the Animal class's, because the actual object is of type Dog.
