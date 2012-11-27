package mat.client;

/**
 * generally speaking, this api should cause invocation of setEnabled 
 * on all FocusWidget children contained by an implementor
 */
public interface Enableable {

	public void setEnabled(boolean enabled);
}
