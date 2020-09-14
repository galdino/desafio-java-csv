package br.com.djcsv.dto;

public class ContaDto {
	
	private String agencia;
	private String conta;
	private double saldo;
	private String status;
	private String processado;
	
	public ContaDto() {
	}
	
	public ContaDto(String agencia, String conta, double saldo, String status) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
	}
	
	public ContaDto(String agencia, String conta, double saldo, String status, String processado) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
		this.processado = processado;
	}

	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcessado() {
		return processado;
	}
	public void setProcessado(String processado) {
		this.processado = processado;
	}
}
