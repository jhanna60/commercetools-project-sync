package com.commercetools.project.sync.producttype;

import com.commercetools.project.sync.Syncer;
import com.commercetools.sync.producttypes.ProductTypeSync;
import com.commercetools.sync.producttypes.ProductTypeSyncOptions;
import com.commercetools.sync.producttypes.ProductTypeSyncOptionsBuilder;
import com.commercetools.sync.producttypes.helpers.ProductTypeSyncStatistics;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeDraft;
import io.sphere.sdk.producttypes.ProductTypeDraftBuilder;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProductTypeSyncer
    extends Syncer<
        ProductType,
        ProductTypeDraft,
        ProductTypeSyncStatistics,
        ProductTypeSyncOptions,
        ProductTypeQuery,
        ProductTypeSync> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductTypeSyncer.class);

  /** Instantiates a {@link Syncer} instance. */
  private ProductTypeSyncer(
      @Nonnull final ProductTypeSync productTypeSync,
      @Nonnull final ProductTypeQuery query,
      @Nonnull final SphereClient sourceClient,
      @Nonnull final SphereClient targetClient) {
    super(productTypeSync, query, sourceClient, targetClient);
  }

  @Nonnull
  public static ProductTypeSyncer of(
      @Nonnull final SphereClient sourceClient, @Nonnull final SphereClient targetClient) {

    final ProductTypeSyncOptions syncOptions =
        ProductTypeSyncOptionsBuilder.of(targetClient)
            .errorCallback(LOGGER::error)
            .warningCallback(LOGGER::warn)
            .build();

    final ProductTypeSync productTypeSync = new ProductTypeSync(syncOptions);

    return new ProductTypeSyncer(
        productTypeSync, ProductTypeQuery.of(), sourceClient, targetClient);
  }

  @Nonnull
  @Override
  protected List<ProductTypeDraft> transformResourcesToDrafts(
      @Nonnull final List<ProductType> page) {
    return page.stream()
        .map(productType -> ProductTypeDraftBuilder.of(productType).build())
        .collect(Collectors.toList());
  }
}
