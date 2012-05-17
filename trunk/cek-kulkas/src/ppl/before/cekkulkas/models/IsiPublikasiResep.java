package ppl.before.cekkulkas.models;


/**
 * Isi Publikasi Resep adalah isi publikasi suatu resep ke jejaring sosial.
 * @author Team Before
 *
 */
public class IsiPublikasiResep {
	
	/** Kirim ke Facebook */
	public static final int FACEBOOK = 0;
	
	/** Kirim ke Twitter */
	public static final int TWITTER = 1;
	
	/** Resep yang ingin dikirim */
	private Resep resep;
	
	/** Komentar */
	private String komentar;
	
	/** Foto */
	private String foto;
	
	/** Tujuan publikasi */
	private int tujuan;
	
	/**
	 * Mendapatkan resep
	 * @return Resep
	 */
	public Resep getResep() {
		return resep;
	}
	
	/**
	 * Mengganti resep
	 * @param resep Resep
	 */
	public void setResep(Resep resep) {
		this.resep = resep;
	}
	
	/**
	 * Mendapatkan komentar
	 * @return Komentar
	 */
	public String getKomentar() {
		return komentar;
	}
	
	/**
	 * Mengganti komentar
	 * @param komentar Komentar
	 */
	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}
	
	/**
	 * Mendapatkan foto
	 * @return Foto
	 */
	public String getFoto() {
		return foto;
	}
	
	/**
	 * Mengganti foto
	 * @param foto Foto
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	/**
	 * Mendapatkan tujuan publikasi
	 * @return Tujuan publikasi
	 */
	public int getTujuan() {
		return tujuan;
	}
	
	/**
	 * Mengganti tujuan publikasi
	 * @param tujuan Tujuan publikasi
	 */
	public void setTujuan(int tujuan) {
		this.tujuan = tujuan;
	}
	
	
}
