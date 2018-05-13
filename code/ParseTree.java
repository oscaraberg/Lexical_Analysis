// Författare: Per Austrin

// Ett syntaxträd
abstract class ParseTree {
	abstract public String process();
}

// Ett syntaxträd som representerar en "leaf(...)"
class LeafNode extends ParseTree {
	Integer data;
	public LeafNode(Integer data) {
		this.data = data;
	}
	public String process() {
		return data.toString();
	}
}

// Ett syntaxträd som representerar en "branch(... , ...)"
class BranchNode extends ParseTree {
	ParseTree left, right;
	public BranchNode(ParseTree left, ParseTree right) {
		this.left = left;
		this.right = right;
	}
	public String process() {
		return "[" + right.process() + ";" + left.process() + "]";
	}
}
