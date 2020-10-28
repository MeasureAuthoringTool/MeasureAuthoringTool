<!DOCTYPE html>
<html lang="en">
    <head>
        <title>${model.measureInformation.ecqmTitle}</title>
        <#include "human_readable_css.ftl" />
    </head>
    <body>
        <section><#include "measure_information_table.ftl"/></section>
        <section><#include "measure_set_table.ftl" /></section>
    </body>
</html>