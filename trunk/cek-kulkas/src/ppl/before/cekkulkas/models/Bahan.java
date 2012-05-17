package ppl.before.cekkulkas.models;

import java.io.Serializable;


/**
 * <p>Bahan merupakan sesuatu yang diolah menjadi suatu masakan.
 * Bahan yang dimaksud di sini merepresentasikan 2 hal, yaitu:
 * <ul>
 *    <li>Bahan yang disebutkan di resep</li>
 *    <li>Bahan yang ada di kulkas</li>
 * </ul>
 * Secara umum, bahan memiliki nama, jumlah dan satuan.
 * </p>
 * @author Team Before
 *
 */
public class Bahan implements Serializable {
	
	/** ID hasil generate otomatis */
	private static final long serialVersionUID = 3805987480582480183L;
	
	/** Nama bahan */
	private String nama;
	
	/** Jumlah bahan */
	private float jumlah;
	
	/** Satuan bahan */
	private String satuan;
	
	/** Status pilih bahan */
	private boolean isSelected;
	
	/**
	 * Constructor objek bahan
	 * @param nama Nama bahan
	 * @param jumlah Jumlah bahan
	 * @param satuan Satuan bahan
	 */
	public Bahan(String nama, float jumlah, String satuan) {
		this.nama = nama;
		this.jumlah = jumlah;
		this.satuan = satuan;
	}
	
	/**
	 * Mendapatkan nama bahan
	 * @return Nama bahan
	 */
	public String getNama() {
		return nama;
	}
	
	/**
	 * Mengganti nama bahan
	 * @param nama Nama bahan
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	/**
	 * Mendapatkan jumlah bahan
	 * @return Jumlah bahan
	 */
	public float getJumlah() {
		return jumlah;
	}
	
	/**
	 * Mengganti jumlah bahan
	 * @param jumlah Jumlah bahan
	 */
	public void setJumlah(float jumlah) {
		this.jumlah = jumlah;
	}
	
	/**
	 * Mendapatkan satuan bahan
	 * @return Satuan bahan
	 */
	public String getSatuan() {
		return satuan;
	}
	
	/**
	 * Mengganti satuan bahan
	 * @param satuan Satuan bahan
	 */
	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}
	
	/**
	 * Mendapatkan status pilih bahan
	 * @return Status pilih bahan
	 */
	public boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * Mengganti status pilih bahan
	 * @param isSelected Status pilih bahan
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
