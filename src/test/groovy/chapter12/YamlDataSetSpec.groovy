package chapter12

import spock.lang.Specification
import chapter12.YamlDataSet
import org.yaml.snakeyaml.Yaml
import chapter12.YamlTable

class YamlDataSetSpec extends Specification {

  def "fixtures.yamlを読み込んでusersを取得できる"() {
    given:
    def usersTable  = new YamlDataSet(getClass().getResourceAsStream("fixture.yaml")).getTable("users")


    expect:
    usersTable.getValue(index, 'id') == id
    usersTable.getValue(index, 'name') == name

    where:
    index | id | name
    0     | 1  | '一郎'
    1     | 2  | '次郎'
  }

}