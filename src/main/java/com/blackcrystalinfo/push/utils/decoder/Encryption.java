package com.blackcrystalinfo.push.utils.decoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Encryption {
	public static final byte EL_Scroll = 0;
	public static final byte EL_Public = 1;
	public static final byte EL_Private = 2;
	public static final byte ET_None = 0;
	public static final byte ET_Des = 1;
	public static final byte ET_Rc4 = 2;

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	private static int scrollKeyList[] = { 0x80DC2C4E, 0x008E971F, 0x785EDBB5,
			0xA05FA956, 0x3BD3E0C7, 0x7E198719, 0x96678EEA, 0xBFF7A813,
			0xD36A1CFC, 0x9EBE4184, 0x240C92BA, 0x19F37C1F, 0xEAF33BE4,
			0xCDF18404, 0xC3475FF9, 0xF004969C, 0x9707DD2D, 0x2A7DA086,
			0x6B9EB8D9, 0xDA3982E5, 0x73FB2401, 0xEEB41F36, 0x7EBA7E89,
			0xD3FBE025, 0xA3FED30B, 0x3E2C82FF, 0x849EE13E, 0x233A5B9A,
			0x3C59B8C0, 0x7D790376, 0x9FC5AF0C, 0x12F08D82, 0x8F270486,
			0x248291DD, 0x19C532BA, 0x51725955, 0xF08D11B1, 0x0942CF40,
			0xC45DBF16, 0x1A574906, 0xD89116A3, 0x69C85997, 0x04880533,
			0x7EEA6881, 0x926FDD1F, 0xD0532BA1, 0x5AD6B8DC, 0x2B966A7D,
			0xE2EF58F8, 0xF83E4703, 0xA89903EC, 0x984FE284, 0xDA92053E,
			0xF83EA532, 0xBA572E2E, 0xE9CF8D3D, 0x684FDD6F, 0x847FBD7D,
			0xA499D9B4, 0x8E302F73, 0x770E0829, 0xA8674555, 0x4BC72789,
			0x274B819C, 0x1802AAE3, 0x7C49B54F, 0x7C9985CB, 0x0CEE2997,
			0xBCFBEC81, 0x4E4EA1A7, 0x0D85B0E0, 0x4B209E1D, 0x896EF8B5,
			0xA0071899, 0x6D04DF13, 0x981F06C1, 0x4BF36217, 0x1CF14F1D,
			0x86607638, 0xE826FB85, 0x0A13143E, 0x090EA624, 0x17420EE0,
			0x8C4D3A6E, 0x9CD597D7, 0xC1EF6C15, 0x9D68F511, 0x7D068E60,
			0xCACE22E8, 0x58CC9205, 0x76291CBB, 0xB494FFD6, 0x5D5F570F,
			0xA4D8D2AB, 0xD993E857, 0xAC5E4CFA, 0xE31835F4, 0x6A4FA358,
			0xFA99B181, 0xA9B8F21B, 0xA28AD601, 0x4B4261BC, 0xB7889BD0,
			0x478BDF9C, 0x64F7F894, 0xBF480891, 0xA2820E1A, 0xE45D3022,
			0x2D8D71B3, 0xDE56D695, 0x0F3DBC07, 0xD7334393, 0xD151BF5F,
			0x387504A2, 0xF5C42972, 0x40B40142, 0x22E781D5, 0xF557EF52,
			0xCAD2200F, 0x5A602057, 0xD3D0B9A6, 0x20D2CBC3, 0xE3C1A646,
			0x41585920, 0x54686973, 0x27732047, 0x55525520, 0x5061742E };

	// public static void main(String[] args) {
	// String aa = "this test ����";
	// try {
	// byte bb[] = decode("A5f4ZzxmCA1yCaXa");
	// System.out.println(bb.length);
	// bb = decryptData(bb, EncryptLevel.EL_Scroll, EncryptType.ET_Rc4, 123,
	// (short) 456, 789, 012, "test");
	// System.out.println(new String(bb, "UTF-8") +"******fsfsdfasfasdf"+
	// bb.length);
	// bb = encryptData(bb, EncryptLevel.EL_Scroll, EncryptType.ET_Rc4, 123,
	// (short) 456, 789, 012, "test");
	//
	// System.out.println(encode(bb)+"******fsdfasfasdf"+ bb.length);
	//
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// public static String encodeToBase64String(byte[] data) {
	// int start = 0;
	// int len = data.length;
	// StringBuffer buf = new StringBuffer(data.length * 3 / 2);
	//
	// int end = len - 3;
	// int i = start;
	// int n = 0;
	//
	// while (i <= end) {
	// int d = ((((int) data[i]) & 0x0ff) << 16)
	// | ((((int) data[i + 1]) & 0x0ff) << 8)
	// | (((int) data[i + 2]) & 0x0ff);
	//
	// buf.append(legalChars[(d >> 18) & 63]);
	// buf.append(legalChars[(d >> 12) & 63]);
	// buf.append(legalChars[(d >> 6) & 63]);
	// buf.append(legalChars[d & 63]);
	//
	// i += 3;
	//
	// if (n++ >= 14) {
	// n = 0;
	// buf.append(" ");
	// }
	// }
	//
	// if (i == start + len - 2) {
	// int d = ((((int) data[i]) & 0x0ff) << 16)
	// | ((((int) data[i + 1]) & 255) << 8);
	//
	// buf.append(legalChars[(d >> 18) & 63]);
	// buf.append(legalChars[(d >> 12) & 63]);
	// buf.append(legalChars[(d >> 6) & 63]);
	// buf.append("=");
	// } else if (i == start + len - 1) {
	// int d = (((int) data[i]) & 0x0ff) << 16;
	//
	// buf.append(legalChars[(d >> 18) & 63]);
	// buf.append(legalChars[(d >> 12) & 63]);
	// buf.append("==");
	// }
	//
	// return buf.toString();
	// }
	// public static byte[] decode(String s) {
	//
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// try {
	// decode(s, bos);
	// } catch (IOException e) {
	// throw new RuntimeException();
	// }
	// byte[] decodedBytes = bos.toByteArray();
	// try {
	// bos.close();
	// bos = null;
	// } catch (IOException ex) {
	// System.err.println("Error while decoding BASE64: " + ex.toString());
	// }
	// return decodedBytes;
	// }
	// private static int decode(char c) {
	// if (c >= 'A' && c <= 'Z')
	// return ((int) c) - 65;
	// else if (c >= 'a' && c <= 'z')
	// return ((int) c) - 97 + 26;
	// else if (c >= '0' && c <= '9')
	// return ((int) c) - 48 + 26 + 26;
	// else
	// switch (c) {
	// case '+':
	// return 62;
	// case '/':
	// return 63;
	// case '=':
	// return 0;
	// default:
	// throw new RuntimeException("unexpected code: " + c);
	// }
	// }
	// private static void decode(String s, OutputStream os) throws IOException
	// {
	// int i = 0;
	//
	// int len = s.length();
	//
	// while (true) {
	// while (i < len && s.charAt(i) <= ' ')
	// i++;
	//
	// if (i == len)
	// break;
	//
	// int tri = (decode(s.charAt(i)) << 18)
	// + (decode(s.charAt(i + 1)) << 12)
	// + (decode(s.charAt(i + 2)) << 6)
	// + (decode(s.charAt(i + 3)));
	//
	// os.write((tri >> 16) & 255);
	// if (s.charAt(i + 2) == '=')
	// break;
	// os.write((tri >> 8) & 255);
	// if (s.charAt(i + 3) == '=')
	// break;
	// os.write(tri & 255);
	//
	// i += 4;
	// }
	// }

	// ���ܷ���
	public static byte[] decryptData(byte[] data, byte level, byte type,
			int time, short sequence, long mac, long srcID, String passwd) {
		byte key[] = null;
		byte result[] = null;
		switch (level) {
		case EL_Scroll:
		case EL_Public:
			key = buildScrolKey(time, srcID);
			break;
		case EL_Private:
			key = buildPrivatekey(passwd, time, mac);
		default:
			break;
		}
		switch (type) {
		case ET_None:
			return data;
		case ET_Des:
			result = decryptUseDes(data, key);
			break;
		case ET_Rc4:
			result = HloveyRc4(data, key);
			break;
		}
		return result;
	}

	// ���ܷ���
	public static byte[] encryptData(byte[] data, byte level, byte type,
			int time, short sequence, long mac, long srcID, String passwd) {
		byte key[] = null;
		byte result[] = null;
		switch (level) {
		case EL_Scroll:
		case EL_Public:
			key = buildScrolKey(time, srcID);
			break;
		case EL_Private:
			key = buildPrivatekey(passwd, time, mac);
		default:
			break;
		}
		switch (type) {
		case ET_None:
			return data;
		case ET_Des:
			result = encryptUseDes(data, key);
			break;
		case ET_Rc4:
			result = HloveyRc4(data, key);
			break;
		}
		return result;
	}

	public static byte[] HloveyRc4(byte[] plaintext, byte[] key) {
		byte[] S = new byte[256];
		byte[] T = new byte[256];
		int keylen;

		if (key.length < 1 || key.length > 256) {
			return null;
		} else {
			keylen = key.length;
			for (int i = 0; i < 256; i++) {
				S[i] = (byte) i;
				T[i] = (byte) (key[i % keylen] & 0xff);
			}
			int j = 0;
			for (int i = 0; i < 256; i++) {
				j = (j + S[i] + T[i]) % 256;
				j &= 0xff;
				S[i] ^= S[j];
				S[j] ^= S[i];
				S[i] ^= S[j];
			}
		}

		byte[] ciphertext = new byte[plaintext.length];
		int i = 0, j = 0, k, t;
		for (int counter = 0; counter < plaintext.length; counter++) {
			i = (i + 1) % 256;
			j = (j + S[i]) % 256;
			j &= 0xff;
			S[i] ^= S[j];
			S[j] ^= S[i];
			S[i] ^= S[j];
			t = (S[i] + S[j]) % 256;
			t &= 0xff;
			k = S[t];
			ciphertext[counter] = (byte) (plaintext[counter] ^ k);
		}
		return ciphertext;
	}

	/**
	 * DES����
	 * 
	 * @param content
	 *            ���������
	 * @param key
	 *            ���ܵ���Կ
	 * @return
	 */
	public static byte[] encryptUseDes(byte[] content, byte[] key) {
		try {
			if (content == null) {
				return new byte[0];
			}
			int length = content.length;
			byte j = (byte) (8 - (length % 8));
			if (j > 0 && j < 8) {
				j += 8;
				byte temp[] = new byte[length + j];
				for (int i = length; i < temp.length; i++) {
					temp[i] = j;
				}
				System.arraycopy(content, 0, temp, 0, content.length);
				;
				content = temp;
			}
			for (int i = 0; i < key.length; i++) {
				key[i] &= 0x7f;
			}
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES����
	 * 
	 * @param content
	 *            ���������
	 * @param key
	 *            ���ܵ���Կ
	 * @return
	 */
	public static byte[] decryptUseDes(byte[] content, byte[] key) {
		try {
			for (int i = 0; i < key.length; i++) {
				key[i] &= 0x7f;
			}
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			byte[] result = cipher.doFinal(content);
			byte j = result[result.length - 1];
			if (j > 0 && j < 16) {
				byte temp[] = new byte[j];
				System.arraycopy(result, result.length - j, temp, 0, j);
				for (byte b : temp) {
					if (b != j) {
						return result;
					}
				}
				temp = new byte[result.length - j];
				System.arraycopy(result, 0, temp, 0, temp.length);
				result = temp;
			}
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	// ����������Կ
	private static byte[] buildScrolKey(int time, long srcID) {
		long key = scrollKeyList[time & 0x7f] & 0x00000000ffffffffL;
//		System.out.println("新头 滚动密钥" + Long.toHexString(key) + "*");
		key = key << 32;
//		System.out.println("新头 滚动密钥" + Long.toHexString(key) + "**");

		long temp = time;
		temp = temp & 0x00000000ffffffffL;
		key = key | temp;
//		System.out.println("新头 滚动密钥" + Long.toHexString(key) + "***"
//				+ Long.toHexString(temp));
		key ^= srcID;
//		System.out.println("新头 滚动密钥" + Long.toHexString(key) + "****"
//				+ Long.toHexString(srcID));
		byte b[] = new byte[8];
//		System.out.println("新头 滚动密钥" + Long.toHexString(key));

		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (key & 0xff);
			key = key >> 8;
		}
		return b;
	}

	// ����˽Կ
	private static byte[] buildPrivatekey(String passwd, int time, long mac) {
		byte[] data = MD5Encode(passwd.getBytes());
		data = MD5Encode(data);

		long ago = 0;
		long after = 0;
		if (data.length < 16) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.allocate(data.length).order(
				ByteOrder.LITTLE_ENDIAN);
		;
		bb.put(data);
		ago = bb.getLong(0);
		after = bb.getLong(8);
//		System.out.println("新头 私钥" + Long.toHexString(ago) + "*");
//		System.out.println("新头 私钥" + Long.toHexString(after) + "**");

		ago ^= after;
		ago ^= mac;
//		System.out.println("新头 私钥" + Long.toHexString(ago) + "****");

		long key = scrollKeyList[time & 0x7f] & 0x00000000ffffffffL;
//		System.out.println("新头 私钥" + Long.toHexString(key) + "f");
		key = key << 32;
//		System.out.println("新头 私钥" + Long.toHexString(key) + "******");
		key |= time & 0x00000000ffffffffL;
//		System.out.println("新头 私钥" + Long.toHexString(key) + "*******");
		key ^= ago;
//		System.out.println("新头 私钥" + Long.toHexString(key) + "********");

		byte b[] = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (key & 0xff);
			key = key >> 8;
		}
		return b;
	}

	// MD5
	public static byte[] MD5Encode(byte[] origin) {
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = md.digest(origin);
		} catch (Exception ex) {
		}
		return result;
	}
}
