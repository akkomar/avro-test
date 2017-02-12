import java.io.ByteArrayOutputStream

import example.avro.{Person, PersonNewFieldAtEnd, PersonNewFieldInTheBeginning}
import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

object Test extends App {

  val person = Person("SomeNa", Some(1234l), List("daydreaming", "hacking"))

  println(person)


  val personDatumWriter = new SpecificDatumWriter[Person](Person.SCHEMA$)

  val byteArrayOutptutStream = new ByteArrayOutputStream()
  val binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutptutStream, null)

  personDatumWriter.write(person, binaryEncoder)
  binaryEncoder.flush()

  val byteArray = byteArrayOutptutStream.toByteArray

  println("SerializedSize: " + byteArray.length)

  import example.proto

  val protoPersonModified = proto.PersonChanged.PersonModified("SomeNa", Some(1234l),Some("newField"), List("daydreaming", "hacking"))
  val protoByteArray = protoPersonModified.toByteArray
println("Serialized modified proto person: "+protoPersonModified)
  println("Serialized proto size: " + protoByteArray.length)

  val protoPerson = proto.Person.Person.parseFrom(protoByteArray)
  println("Deserialized Proto person:"+protoPerson)


  val person2 = PersonNewFieldAtEnd("SomeNa", Some(1234l), List("daydreaming", "hacking"), "newFieldValue")
  val person2Ser = serializeAvroPersonWithNewFieldAtEnd(person2)
  println("Serializing: " + person2)
  val person3 = deserializeAvroPerson(person2Ser)
  println("Deserialized: " + person3)


  val person4 = PersonNewFieldInTheBeginning(Some("newFieldBeginning"), "SomeNa", Some(1234l), List("daydreaming", "hacking"))
  val person4Ser = serializeAvroPersonWithNewFieldInTheBeginning(person4)
  println("Serializing: " + person4)
  try {
    val person5 = deserializeAvroPerson(person4Ser)
    println("Deserialized: " + person5)
    assert(false, "should throw here")
  } catch {
    case e: java.lang.ArrayIndexOutOfBoundsException => ()
  }


  println("Deserializing old avro person with new schema...")
  val newPerson = deserializeAvroPersonWithNewFieldInTheBeginning(byteArray)
  println(newPerson)


  def serializeAvroPersonWithNewFieldAtEnd(person: PersonNewFieldAtEnd): Array[Byte] = {
    val personWithNewFieldAtEndDatumWriter = new SpecificDatumWriter[PersonNewFieldAtEnd](PersonNewFieldAtEnd.SCHEMA$)
    val byteArrayOutptutStream = new ByteArrayOutputStream()
    val binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutptutStream, null)

    personWithNewFieldAtEndDatumWriter.write(person, binaryEncoder)
    binaryEncoder.flush()

    val byteArray = byteArrayOutptutStream.toByteArray

    byteArray
  }

  def serializeAvroPersonWithNewFieldInTheBeginning(person: PersonNewFieldInTheBeginning): Array[Byte] = {
    val personWithNewFieldAtEndDatumWriter = new SpecificDatumWriter[PersonNewFieldInTheBeginning](PersonNewFieldInTheBeginning.SCHEMA$)
    val byteArrayOutptutStream = new ByteArrayOutputStream()
    val binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutptutStream, null)

    personWithNewFieldAtEndDatumWriter.write(person, binaryEncoder)
    binaryEncoder.flush()

    val byteArray = byteArrayOutptutStream.toByteArray

    byteArray
  }

  def deserializeAvroPerson(serialized: Array[Byte]): Person = {
    val reader = new SpecificDatumReader[Person](Person.SCHEMA$)

    val binaryDecoder = DecoderFactory.get().binaryDecoder(serialized, null)

    reader.read(null, binaryDecoder)
  }

  def deserializeAvroPersonWithNewFieldInTheBeginning(serialized: Array[Byte]): PersonNewFieldInTheBeginning = {
    val reader = new SpecificDatumReader[PersonNewFieldInTheBeginning](Person.SCHEMA$,PersonNewFieldInTheBeginning.SCHEMA$)

    val binaryDecoder = DecoderFactory.get().binaryDecoder(serialized, null)

    reader.read(null, binaryDecoder)
  }


}
