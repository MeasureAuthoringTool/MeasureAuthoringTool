package mat.client.measure.measuredetails.components;

public class TransmissionFormatModel implements MeasureDetailsComponentModel{
	private String transmissionFormat;

	public String getTransmissionFormat() {
		return transmissionFormat;
	}

	public void setTransmissionFormat(String transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
}
