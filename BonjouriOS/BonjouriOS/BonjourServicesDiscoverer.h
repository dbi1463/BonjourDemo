//
//  BonjourServicesDiscoverer.h
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "BonjourDiscoveredServices.h"

@interface BonjourServicesDiscoverer : NSObject<NSNetServiceBrowserDelegate>

- (instancetype)initWithContainer:(id<BonjourDiscoveredServices>)container;

- (void)start;

- (void)stop;

@property (nonatomic, readonly) id<BonjourDiscoveredServices> serviceContainer;

@end
