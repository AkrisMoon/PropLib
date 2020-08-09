<h4>Запуск проекта:</h4>
<b>out</b>: <br><b>PropLib.jar</b> - запускаемый файл со всеми зависимостями.
<h4>Инструкция по запуску из командной строки:</h4>
 Для запуска из коммандной строки файлу <b>PropLib.jar</b> необходимо передать аргумент с расширением <b>*.properties</b>, в котором находятся значения полей класса.<br> Пример консольной комманды:<br>
<br>
<code>java -jar PropLib.jar config.properties</code><br>
<br>
<h4>Содержание файла <b>*.properties</b>:</h4>
Файл может содержать JSON-строку. Значение полей класса имеют следующий синтаксис:<br>
<code># Имя параметра = значение параметра</code><br>
<code>com.mycompany.name = SuperSoft</code><br>
<code>com.mycompany.Owner = Me</code><br>
<code>com.mycompany.address = {street: Pushkina, home: Kolotushkina}</code><br>
<code>com.mycompany.years.old = 1</code><br>


