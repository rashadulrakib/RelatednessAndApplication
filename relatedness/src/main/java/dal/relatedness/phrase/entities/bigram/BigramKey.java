package dal.relatedness.phrase.entities.bigram;

public class BigramKey {
	public Integer keyUni1;
	public Integer keyUni2;
	
	public BigramKey(int keyUni1, int keyUni2){
		this.keyUni1 = keyUni1;
		this.keyUni2 = keyUni2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyUni1 == null) ? 0 : keyUni1.hashCode());
		result = prime * result + ((keyUni2 == null) ? 0 : keyUni2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BigramKey))
			return false;
		BigramKey other = (BigramKey) obj;
		if (keyUni1 == null) {
			if (other.keyUni1 != null)
				return false;
		} else if (!keyUni1.equals(other.keyUni1))
			return false;
		if (keyUni2 == null) {
			if (other.keyUni2 != null)
				return false;
		} else if (!keyUni2.equals(other.keyUni2))
			return false;
		return true;
	}
}
