package chapter12

import org.apache.commons.io.IOUtils
import spock.lang.Specification
import chapter12.YamlDataSet
import org.yaml.snakeyaml.Yaml
import chapter12.YamlTable
import java.io.InputStream


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

  def "YAMLを読み込んでusersを取得できる"() {
    given:
    def yamlIn = IOUtils.toInputStream(
            '''users:
  - id: 1
    name: 一郎
  - id: 2
    name: 次郎''')

    def usersTable = new YamlDataSet(yamlIn).getTable("users")

    expect:
    usersTable.getValue(index, 'id') == id
    usersTable.getValue(index, 'name') == name

    where:
    index | id | name
    0     | 1  | '一郎'
    1     | 2  | '次郎'

  }
  def "YAMLを読み込んでusersを取得できる、2レコード目のカラム順が違う"() {
    given:
    def yamlIn = IOUtils.toInputStream(
            '''users:
  - id: 1
    name: 一郎
  - name: 次郎
    id: 2
  - id: 3
    name: 三郎''')

    def usersTable = new YamlDataSet(yamlIn).getTable("users")

    expect:
    usersTable.getValue(index, 'id') == id
    usersTable.getValue(index, 'name') == name

    where:
    index | id | name
    0     | 1  | '一郎'
    1     | 2  | '次郎'
    2     | 3  | '三郎'

  }
}