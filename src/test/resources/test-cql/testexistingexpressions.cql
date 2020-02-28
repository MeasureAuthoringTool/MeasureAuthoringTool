library testexistingexpressions version '0.0.000'

using QDM version '5.4'

parameter "TestParameter" Boolean

context Patient

define "testdefinition":
  true
  
define function "testfunction"(arg1 Boolean):
  true

