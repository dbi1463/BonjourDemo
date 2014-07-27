//
//  BonjourDiscoveredServices.h
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "BonjourDiscoveredServicesDelegate.h"

@interface BonjourDiscoveredServices : NSObject<NSNetServiceBrowserDelegate, UITableViewDataSource>

- (void)startDiscovery;

- (void)stopDiscovery;

@property (weak, nonatomic) id<BonjourDiscoveredServicesDelegate> delegate;

@end
