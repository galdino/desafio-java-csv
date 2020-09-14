# Desafio Java CSV

Aplicação Spring Boot para processar arquivos CSV.

Para executar aplicação, utilizar o .jar(desafio-java-csv-0.0.1-SNAPSHOT.jar) e o CSV exemplo(contas.csv) em (../desafio-java-csv/exemplo).

Com estes dois arquivos em um mesmo diretório, executar:
```
java -jar desafio-java-csv-0.0.1-SNAPSHOT.jar contas.csv 
```

CSV exemplo(contas.csv) após processamento:
```
agencia;conta;saldo;status;processado
0101;12225-6;100,00;A;S
0101;12226-8;3200,50;A;S
3202;40011-1;-35,12;I;S
3202;54001-2;0,00;P;S
3202;00321-2;34500,00;B;S 
```