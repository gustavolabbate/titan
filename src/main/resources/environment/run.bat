cls && Color 0F
@echo off
rem setlocal
@set MEM=-Xms512m -Xmx1024m
@set titan=titan.jar


SET /P xml=xml a ser processado: (sem o .xml) 

java -jar %MEM% %titan% "%xml%.xml"

