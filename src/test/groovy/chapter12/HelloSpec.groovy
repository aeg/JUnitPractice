package chapter12;

import spock.lang.Specification

class HelloSpec extends Specification {

  def "あああ"() {
    given:

    println "hello"
expect:
    true

  }
}