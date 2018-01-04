package dal.relatedness.phrase.utils.encoding;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class EliasOmega {

	public static class EliasOmegaDecoder {
		final byte[] _buffer;
		final int _len;
		private int _pos;

		public EliasOmegaDecoder(byte[] buffer, int pos, int length)
				throws Exception {
			if (pos < 0 || pos > buffer.length)
				throw new Exception("pos");
			if (length < 0 || pos + length > buffer.length)
				throw new Exception("length");

			_buffer = buffer;
			_len = length * 8;
			_pos = pos * 8;
		}

		public EliasOmegaDecoder(byte[] buffer) throws Exception {
			this(buffer, 0, buffer.length);
		}

		boolean GetBit() throws Exception {
			if (_pos >= _len)
				throw new Exception("Buffer overrun");
			boolean r = (_buffer[_pos / 8] & (1 << (_pos % 8))) != 0;
			++_pos;
			return r;
		}

		// / <summary>
		// / Read the next value from the stream
		// / </summary>
		// / <returns>Decoded value</returns>
		public long ReadValue() {
			long num = 1L;

			try {

				while (GetBit()) {
					long len = num;
					num = 1;
					for (int i = 0; i < len; ++i) {
						num <<= 1;
						if (GetBit())
							num |= 1;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return num;
		}
	}

	public static class EliasOmegaEncoder {

		ByteArrayOutputStream bosArrayOutputStream;

		byte _c;
		int _p;

		public EliasOmegaEncoder() {
		}

		public byte[] EncodeInterpolate(List<Integer> sortedIdsAsc) {
			try {
				
				bosArrayOutputStream = new ByteArrayOutputStream();
				
				_c = 0;
				_p = 0;
				
				if (sortedIdsAsc == null || sortedIdsAsc.size() < 1) {
					return null;
				}

				int firstNumber = sortedIdsAsc.get(0);
				this.Append(firstNumber);

				int last = firstNumber;
				for (int i = 1; i < sortedIdsAsc.size(); i++) {					
					this.Append(sortedIdsAsc.get(i) - last);
					last = sortedIdsAsc.get(i);
				}

				return this.GetByteArray();
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		void WriteBit(boolean bit) {
			try {
				if (bit)
					_c |= (byte) (1 << _p);
				_p += 1;
				if (_p < 8)
					return;
				bosArrayOutputStream.write(_c);
				_c = 0;
				_p = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// / <summary>
		// / Append a integer to the stream
		// / </summary>
		// / <param name="value">Integer to encode. Must be greater than
		// one.</param>
		private void Append(long value) {
			try {
				if (value < 1)
					//throw new Exception("value must be greater than 1");
					return;
				if (_p < 0)
					throw new Exception("BufferEncoder is closed");
				boolean[] t = new boolean[8192];
				int l = 0;
				while (value > 1) {
					int len = 0;
					for (long temp = value; temp > 0; temp >>= 1)
						// calculate 1+floor(log2(num))
						len++;
					for (int i = 0; i < len; i++)
						t[l++] = ((value >> i) & 1) != 0;
					value = len - 1;
				}
				for (--l; l >= 0; --l)
					WriteBit(t[l]);
				
				WriteBit(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// / <summary>
		// / Get the result buffer. Close the stream.
		// / </summary>
		// / <returns>Encoded buffer</returns>
		private byte[] GetByteArray() {
			try {
				if (_p != 0)
					bosArrayOutputStream.write(_c);

				byte[] r = bosArrayOutputStream.toByteArray();
				_p = -1;
				bosArrayOutputStream.close();

				bosArrayOutputStream = null;
				
				return r;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
