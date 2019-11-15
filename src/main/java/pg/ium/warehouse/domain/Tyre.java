package pg.ium.warehouse.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pg.ium.warehouse.exception.TyreQuantityLowerThanZeroException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Data
@NoArgsConstructor(access = PROTECTED)
public class Tyre {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String producer;
	private String name;
	@Column(name = "rim_size")
	private int rimSize;
	private int quantity;
	@Column(precision = 10, scale = 2)
	private BigDecimal price;

	@Builder
	private Tyre(String producer, String name, int rimSize, int quantity, BigDecimal price) {
		this.producer = producer;
		this.name = name;
		this.rimSize = rimSize;
		this.quantity = quantity;
		this.price = price;
	}

	public void replaceFields(@NonNull Tyre other) {
		setName(other.name);
		setPrice(other.price);
		setProducer(other.producer);
		setQuantity(other.quantity);
		setRimSize(other.rimSize);
	}

	public void changeQuantity(int change) throws TyreQuantityLowerThanZeroException {
		int newQuantity = getQuantity() + change;
		if (newQuantity < 0) {
			throw new TyreQuantityLowerThanZeroException(this.id);
		}
		setQuantity(newQuantity);
	}
}